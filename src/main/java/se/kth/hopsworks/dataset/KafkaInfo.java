/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.dataset;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class KafkaInfo {
    
    private boolean kafka;
    
    private String schema;

    public KafkaInfo() {
    }

    public KafkaInfo(boolean kafka, String schema) {
        this.kafka = kafka;
        this.schema = schema;
    }

    public boolean isKafka() {
        return kafka;
    }

    public void setKafka(boolean kafka) {
        this.kafka = kafka;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
    
    
    
}
