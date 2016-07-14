/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import se.kth.hopsworks.hops_site.register.RegisterJson;
import se.kth.hopsworks.hops_site.popular_datasets.PopularDatasetsJson;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import se.kth.hopsworks.dataset.DatasetStructure;
import se.kth.hopsworks.hops_site.ping.PingJson;
import se.kth.hopsworks.hops_site.ping.SuccessPingJson;
import se.kth.hopsworks.hops_site.popular_datasets.GetPopularDatasetsJson;
import se.kth.hopsworks.hops_site.popular_datasets.SuccessGetPopularDatasetsJson;
import se.kth.hopsworks.hops_site.popular_datasets.PopularDatasets;
import se.kth.hopsworks.hops_site.register.SuccessRegisterJson;
import se.kth.hopsworks.hops_site.register.RegisteredClusters;
import se.kth.hopsworks.util.Settings;

@Startup
@Singleton
public class ManageGlobalClusterParticipation {

    private List<RegisteredClusters> registeredClusters = null;
    private List<PopularDatasets> popularDatasets = null;
    private WebTarget webTarget = null;
    private Client client = null;

    @Resource
    private SessionContext context;

    @EJB
    private Settings settings;

    @PostConstruct
    private void init() {

        DoTimeout();
    }

    @Timeout
    private void TimeoutOcurred() {

        if (webTarget != null && client != null) {
            doTimeoutStuff();
        } else {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(settings.getBASE_URI_HOPS_SITE()).path("myresource");
            doTimeoutStuff();
        }

    }

    private void doTimeoutStuff() {
        if (settings.getCLUSTER_ID() != null) {
            PingAndGetPopularDatasets();
        } else {
            TryToRegister();
        }
    }

    private void TryToRegister() {

        String id = null;
        try {
            String gvodEndpoint = settings.getGVOD_UDP_ENDPOINT();
            if (gvodEndpoint != null) {
                id = RegisterRest(settings.getELASTIC_PUBLIC_RESTENDPOINT(), settings.getCLUSTER_MAIL(), settings.getCLUSTER_CERT(), gvodEndpoint);
            }
        } catch (ClientErrorException ex) {

        } finally {
            if (id != null) {
                settings.setCLUSTER_ID(id);
            }
            DoTimeout();
        }

    }

    private void PingAndGetPopularDatasets() {

        List<RegisteredClusters> pingResponse = null;
        List<PopularDatasets> popularDatasetsResponse = null;
        try {
            pingResponse = PingRest(settings.getCLUSTER_ID());
            popularDatasetsResponse = getPopularDatasets(settings.getCLUSTER_ID());
        } catch (ClientErrorException ex) {

        } finally {
            if (pingResponse != null) {
                this.registeredClusters = pingResponse;
            }
            if (popularDatasetsResponse != null) {
                this.popularDatasets = popularDatasetsResponse;
            }
            DoTimeout();
        }

    }

    private List<RegisteredClusters> PingRest(String clusterId) {
        
        PingJson pingJson = new PingJson(clusterId);

        WebTarget resource = webTarget.path("ping");

        Response r = resource.request().accept(MediaType.APPLICATION_JSON).put(Entity.json(pingJson));

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(SuccessPingJson.class).getClusters();
        } else {
            return null;
        }
    }

    private List<PopularDatasets> getPopularDatasets(String clusterId){
        
        GetPopularDatasetsJson getPopularDatasetsJson = new GetPopularDatasetsJson(clusterId);

        WebTarget resource = webTarget.path("populardatasets");

        Response r = resource.request().accept(MediaType.APPLICATION_JSON).put(Entity.json(getPopularDatasetsJson));

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(SuccessGetPopularDatasetsJson.class).getPopularDatasets();
        } else {
            return null;
        }
    }

    private String RegisterRest(String searchEndpoint, String email, String cert, String gvodEndpoint) {

        RegisterJson registerJson = new RegisterJson(searchEndpoint, gvodEndpoint, email, cert);

        WebTarget resource = webTarget.path("register");
        
        Response r = resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(registerJson));

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(SuccessRegisterJson.class).getClusterId();
        } else {
            return null;
        }

    }

    private void DoTimeout() {

        long duration = 60000;
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        context.getTimerService().createSingleActionTimer(duration, timerConfig);

    }

    public void notifyHopsSiteAboutNewDataset(String name, DatasetStructure datasetStructure, String publicDsId, int size, int leeches, int seeds) {
        
        PopularDatasetsJson addPopularDatasetJson = new PopularDatasetsJson(name, datasetStructure, publicDsId, size,leeches, seeds);
        
        WebTarget resource = webTarget.path("populardatasets");
        
        resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(addPopularDatasetJson));
        
        
    }

    public List<RegisteredClusters> getRegisteredClusters() {
        return registeredClusters;
    }

    public List<PopularDatasets> getPopularDatasets() {
        return popularDatasets;
    }
    
    

}
