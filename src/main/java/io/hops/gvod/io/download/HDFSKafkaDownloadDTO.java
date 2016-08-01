/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.download;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class HDFSKafkaDownloadDTO {
    
    
    private int projectId;
    
    private String publicDatasetId;
    
    private List<String> partners;
    
    private Map topics;
    
    private String destinationDatasetName;

    public HDFSKafkaDownloadDTO() {
    }

    public HDFSKafkaDownloadDTO(int projectId, String publicDatasetId, List<String> partners, Map topics, String destinationDatasetName) {
        this.projectId = projectId;
        this.publicDatasetId = publicDatasetId;
        this.partners = partners;
        this.topics = topics;
        this.destinationDatasetName = destinationDatasetName;
    }

    public String getPublicDatasetId() {
        return publicDatasetId;
    }

    public void setPublicDatasetId(String publicDatasetId) {
        this.publicDatasetId = publicDatasetId;
    }

    public String getDestinationDatasetName() {
        return destinationDatasetName;
    }

    public void setDestinationDatasetName(String destinationDatasetName) {
        this.destinationDatasetName = destinationDatasetName;
    }
    

    public int getProjectId() {
        return projectId;
    }
    public List<String> getPartners() {
        return partners;
    }

    public Map getTopics() {
        return topics;
    }

    public void setTopics(Map topics) {
        this.topics = topics;
    }
     
    
      
}
