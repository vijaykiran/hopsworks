/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site.register;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class RegisteredClusters {

       private String clusterId;
       
       private String email;
       
       private String cert;
       
       private String searchEndpoint;
       
       private String gvodEndpoint;
       
       private int heartbeatsMissed;
       
       private String dateRegistered;
       
       private String dateLastPing;

    public RegisteredClusters(String clusterId, String email, String cert, String gvodEndpoint, int heartbeatsMissed, String dateRegistered, String dateLastPing, String searchEndpoint) {
        this.clusterId = clusterId;
        this.email = email;
        this.cert = cert;
        this.gvodEndpoint = gvodEndpoint;
        this.heartbeatsMissed = heartbeatsMissed;
        this.dateRegistered = dateRegistered;
        this.dateLastPing = dateLastPing;
        this.searchEndpoint = searchEndpoint;
    }
    
    
    public RegisteredClusters() {
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public void setSearchEndpoint(String searchEndpoint) {
        this.searchEndpoint = searchEndpoint;
    }

    public void setGvodEndpoint(String gvodEndpoint) {
        this.gvodEndpoint = gvodEndpoint;
    }

    public void setHeartbeatsMissed(int heartbeatsMissed) {
        this.heartbeatsMissed = heartbeatsMissed;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public void setDateLastPing(String dateLastPing) {
        this.dateLastPing = dateLastPing;
    }

    
    
    public String getSearchEndpoint() {
        return searchEndpoint;
    }
    
    
    
    public String getClusterId() {
        return clusterId;
    }

    public String getEmail() {
        return email;
    }

    public String getCert() {
        return cert;
    }

    public String getGvodEndpoint() {
        return gvodEndpoint;
    }

    public int getHeartbeatsMissed() {
        return heartbeatsMissed;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public String getDateLastPing() {
        return dateLastPing;
    }
       
       
       
}
