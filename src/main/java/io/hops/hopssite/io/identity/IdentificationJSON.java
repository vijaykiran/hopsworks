/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.hopssite.io.identity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class IdentificationJSON {
    
    private String clusterId;

    public IdentificationJSON(String clusterId) {
        this.clusterId = clusterId;
    }
    
    public IdentificationJSON(){
        
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
    
}