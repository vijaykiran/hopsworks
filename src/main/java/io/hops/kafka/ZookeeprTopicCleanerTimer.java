package io.hops.kafka;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kafka.admin.AdminUtils;
import kafka.common.TopicAlreadyMarkedForDeletionException;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import se.kth.hopsworks.rest.AppException;
import se.kth.hopsworks.util.Settings;

@Singleton
public class ZookeeprTopicCleanerTimer {

    private final static Logger LOGGER = Logger.getLogger(KafkaFacade.class.getName());

    public final int connectionTimeout = 30 * 1000;// 30 seconds

    public int sessionTimeoutMs = 30 * 1000;//30 seconds

    @PersistenceContext(unitName = "kthfsPU")
    private EntityManager em;

    @EJB
    Settings settings;

    @EJB
    KafkaFacade kafkaFacade;

    ZkClient zkClient = null;

    ZkConnection zkConnection = null;

    ZooKeeper zk = null;

    @Resource
    private TimerService timerSvc;
    
    @Timeout
    public void synchronizeTopics(){
    
            Set<String> zkTopics = new HashSet<>();
        try {
            if (zk == null || !zk.getState().isConnected()) {
                if (zk != null) {
                    zk.close();
                }
                zk = new ZooKeeper(settings.getZkConnectStr(),
                        sessionTimeoutMs, new ZookeeperWatcher());
            }
            List<String> topics = zk.getChildren("/brokers/topics", false);
            zkTopics.addAll(topics);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to find the zookeeper server: ", ex.toString());
        } catch (KeeperException | InterruptedException ex) {
            LOGGER.log(Level.SEVERE, "Cannot retrieve topic list from Zookeeper", ex.toString());
        }

        List<ProjectTopics> dbProjectTopics = em.createNamedQuery(
                "ProjectTopics.findAll").getResultList();

        Set<String> dbTopics = new HashSet<>();

        for (ProjectTopics pt : dbProjectTopics) {
            try {
                dbTopics.add(pt.getProjectTopicsPK().getTopicName());
            } catch (UnsupportedOperationException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            }
        }

        /*
        Remove topics from database which no more exist in the zookeeper. 
        A Kafka topic has a configurable retention period, defaults to 168 hours,
        in the zookeeper. After this period, the topic and all its logs is deleted.
        Such topics should be removed from the database.
         */
        Set<String> dbTopicsTemp = new HashSet(dbTopics);

        //remove topics from database which do not exist in zookeeper		
        if (!dbTopicsTemp.isEmpty()) {
            dbTopicsTemp.removeAll(zkTopics);
            for (String topicName : dbTopicsTemp) {
                ProjectTopics removeTopic = em.createNamedQuery(
                        "ProjectTopics.findByTopicName", ProjectTopics.class)
                        .setParameter("topicName", topicName).getSingleResult();
                em.remove(removeTopic);
                LOGGER.log(Level.SEVERE, "************************** "
                        + "{0} is being removed from database", new Object[]{topicName});
            }
        }

        /*
        Remove topics from zookeeper which do not exist in database. This situation
        happens when a hopsworks project is deleted, because all the topics in the project
        will be deleted (casecade delete) without deleting them from the Kafka cluster.
        1. get all topics from zookeeper
        2. get the topics which exist in zookeeper, but not in database
            zkTopics.removeAll(dbTopics);
        3. remove those topics
        
        Or during normal system operation:
            1. topic create fails after creating the topic on zookeeper or
            2. topic delete fails after removing the topic from the database.
        
         */
        if (!zkTopics.isEmpty()) {
            zkTopics.removeAll(dbTopics);
            for (String topicName : zkTopics) {
                try {
                    if (zkClient == null) {
                        zkClient = new ZkClient(kafkaFacade.getIp(settings.getZkConnectStr()).getHostName(),
                                sessionTimeoutMs, connectionTimeout, ZKStringSerializer$.MODULE$);
                    }
                } catch (AppException ex) {
                    LOGGER.log(Level.SEVERE, "Unable to get zookeeper ip address ", ex.toString());
                }
                if (zkConnection == null) {
                    zkConnection = new ZkConnection(settings.getZkConnectStr());
                }
                ZkUtils zkUtils = new ZkUtils(zkClient, zkConnection, false);

                try {
                    AdminUtils.deleteTopic(zkUtils, topicName);
                    LOGGER.log(Level.INFO, "{0} is removed from Zookeeper", new Object[]{topicName});
                } catch (TopicAlreadyMarkedForDeletionException ex) {
                    LOGGER.log(Level.INFO, "{0} is already marked for deletion", new Object[]{topicName});
                }
            }
        }
        
        scheduleCheckingServices();
    }

    private class ZookeeperWatcher implements Watcher {

        @Override
        public void process(WatchedEvent we) {
        }
    }

    @PostConstruct
    public void initialise() {

        scheduleCheckingServices();
    }

    private void scheduleCheckingServices() {
        // When finished checking the availability of services, schedule another check 60 seconds later
        timerSvc.createSingleActionTimer(Settings.INTERVAL_MS_SYNCHRONIZE_KAFKA_TOPICS, new TimerConfig());
    }

    public void stop() {
        for (Timer timer : timerSvc.getTimers()) {
            timer.cancel();
        }
    }

}
