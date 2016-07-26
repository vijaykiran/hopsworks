/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.resources;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class KafkaResource {
    
    private String brokerEndpoint;
    private String restEndpoint;
    private String domain;
    private String sessionId;
    private String projectId;
    private Map topics;
    private String keyStore;
    private String trustStore;

    public KafkaResource(String brokerEndpoint, String restEndpoint, String domain, String sessionId,
            String projectId, Map topics, String keyStore, String trustStore) {
        this.brokerEndpoint = brokerEndpoint;
        this.restEndpoint = restEndpoint;
        this.domain = domain;
        this.sessionId = sessionId;
        this.projectId = projectId;
        this.topics = topics;
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

    public String getKeyStore() {
        return keyStore;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public Map getTopics() {
        return topics;
    }

    public void setBrokerEndpoint(String brokerEndpoint) {
        this.brokerEndpoint = brokerEndpoint;
    }

    public void setRestEndpoint(String restEndpoint) {
        this.restEndpoint = restEndpoint;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTopics(Map topics) {
        this.topics = topics;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }
    
    
    
}
