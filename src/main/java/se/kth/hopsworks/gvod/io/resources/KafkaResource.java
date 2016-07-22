/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.resources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class KafkaResource {
    
    private final String brokerEndpoint;
    private final String restEndpoint;
    private final String domain;
    private final String sessionId;
    private final String projectId;
    private final String topicName;
    private final String keyStore;
    private final String trustStore;

    public KafkaResource(String brokerEndpoint, String restEndpoint, String domain, String sessionId,
            String projectId, String topicName, String keyStore, String trustStore) {
        this.brokerEndpoint = brokerEndpoint;
        this.restEndpoint = restEndpoint;
        this.domain = domain;
        this.sessionId = sessionId;
        this.projectId = projectId;
        this.topicName = topicName;
        this.keyStore = keyStore;
        this.trustStore = trustStore;
    }

    public String getBrokerEndpoint() {
        return brokerEndpoint;
    }

    public String getRestEndpoint() {
        return restEndpoint;
    }

    public String getDomain() {
        return domain;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public String getTrustStore() {
        return trustStore;
    }
    
    
    
}
