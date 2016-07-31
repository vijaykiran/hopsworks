/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.resources.items;

import io.hops.gvod.io.resources.HDFSResource;
import io.hops.gvod.io.resources.KafkaResource;
import java.util.Map;

/**
 *
 * @author jsvhqr
 */
public class ExtendedDetails {
    
    private Map<String, HDFSResource> hdfsDetails;
    private Map<String, KafkaResource> kafkaDetails;

    public ExtendedDetails() {
    }

    public ExtendedDetails(Map<String, HDFSResource> hdfsDetails, Map<String, KafkaResource> kafkaDetails) {
        this.hdfsDetails = hdfsDetails;
        this.kafkaDetails = kafkaDetails;
    }

    public Map<String, HDFSResource> getHdfsDetails() {
        return hdfsDetails;
    }

    public void setHdfsDetails(Map<String, HDFSResource> hdfsDetails) {
        this.hdfsDetails = hdfsDetails;
    }

    public Map<String, KafkaResource> getKafkaDetails() {
        return kafkaDetails;
    }

    public void setKafkaDetails(Map<String, KafkaResource> kafkaDetails) {
        this.kafkaDetails = kafkaDetails;
    }
    
    
    
}
