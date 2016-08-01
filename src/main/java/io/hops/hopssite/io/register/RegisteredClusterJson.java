/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.hopssite.io.register;

import io.hops.gvod.io.resources.items.AddressJSON;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class RegisteredClusterJson {

       private String clusterId;
       
       private String searchEndpoint;
       
       private String email;
       
       private String cert;
       
       private AddressJSON gvodEndpoint;
       
       private long heartbeatsMissed;
       
       private String dateRegistered;
       
       private String dateLastPing;

    public RegisteredClusterJson(String clusterId, String email, String cert, AddressJSON gvodEndpoint, long heartbeatsMissed, String dateRegistered, String dateLastPing, String searchEndpoint) {
        this.clusterId = clusterId;
        this.email = email;
        this.cert = cert;
        this.gvodEndpoint = gvodEndpoint;
        this.heartbeatsMissed = heartbeatsMissed;
        this.dateRegistered = dateRegistered;
        this.dateLastPing = dateLastPing;
        this.searchEndpoint = searchEndpoint;
    }
    
    
    public RegisteredClusterJson() {
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

    public void setGvodEndpoint(AddressJSON gvodEndpoint) {
        this.gvodEndpoint = gvodEndpoint;
    }

    public void setHeartbeatsMissed(long heartbeatsMissed) {
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

    public AddressJSON getGvodEndpoint() {
        return gvodEndpoint;
    }

    public long getHeartbeatsMissed() {
        return heartbeatsMissed;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public String getDateLastPing() {
        return dateLastPing;
    }
       
       
       
}
