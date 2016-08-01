/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.resources.items;

/**
 *
 * @author jsvhqr
 */
public class KafkaResource {
    
    private String sessionId;
    private String topicName;

    public KafkaResource(String sessionId, String topicName) {
        this.sessionId = sessionId;
        this.topicName = topicName;
    }
    
    public KafkaResource(){
        
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    
    
    
}
