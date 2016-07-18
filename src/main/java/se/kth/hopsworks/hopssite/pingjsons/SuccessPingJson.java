/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hopssite.pingjsons;

import java.util.List;
import se.kth.hopsworks.hopssite.registerjsons.RegisteredClusters;

/**
 *
 * @author jsvhqr
 */
public class SuccessPingJson {
    
    private List<RegisteredClusters> clusters;
    public SuccessPingJson(){
        
    }
    
    public List<RegisteredClusters> getClusters() {
        return clusters;
    }

    public void setClusters(List<RegisteredClusters> clusters) {
        this.clusters = clusters;
    }
    
    
    
}
