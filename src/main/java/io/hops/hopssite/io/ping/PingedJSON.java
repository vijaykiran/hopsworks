/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.hopssite.io.ping;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import io.hops.hopssite.io.register.RegisteredClusterJSON;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PingedJSON {
    
    
    private List<RegisteredClusterJSON> clusters;
    public PingedJSON(){
        
    }

    public PingedJSON(List<RegisteredClusterJSON> clusters) {
        this.clusters = clusters;
    }
    
    public List<RegisteredClusterJSON> getClusters() {
        return clusters;
    }

    public void setClusters(List<RegisteredClusterJSON> clusters) {
        this.clusters = clusters;
    }
    
    
    
}
