/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.download;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DownloadDTO {
    
    private String destinationDatasetName;
    
    private int projectId;
    
    private String publicDatasetId;
    
    private List<String> partners;
    
    private JSONObject topics; 
        

    public DownloadDTO() {
    }

    public DownloadDTO(String destinationDatasetName, int projectId, String publicDatasetId, List<String> partners, JSONObject topics) {
        this.destinationDatasetName = destinationDatasetName;
        this.projectId = projectId;
        this.publicDatasetId = publicDatasetId;
        this.partners = partners;
        this.topics = topics;
    }

    public String getDestinationDatasetName() {
        return destinationDatasetName;
    }

    public void setDestinationDatasetName(String destinationDatasetName) {
        this.destinationDatasetName = destinationDatasetName;
    }

    public String getPublicDatasetId() {
        return publicDatasetId;
    }

    public void setPublicDatasetId(String publicDatasetId) {
        this.publicDatasetId = publicDatasetId;
    }

    public int getProjectId() {
        return projectId;
    }
    public List<String> getPartners() {
        return partners;
    } 

    public JSONObject getTopics() {
        return topics;
    }

    public void setTopics(JSONObject topics) {
        this.topics = topics;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setPartners(List<String> partners) {
        this.partners = partners;
    }
    
    
    
    
    
}
