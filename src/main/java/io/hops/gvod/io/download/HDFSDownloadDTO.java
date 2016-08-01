/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.download;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class HDFSDownloadDTO {
    
    private String destinationDatasetName;
    
    private int projectId;
    
    private String publicDatasetId;
    
    private List<String> partners;
    
    private Map<String,String> fileTopics;
    

    public HDFSDownloadDTO() {
    }

    public HDFSDownloadDTO(String destinationDatasetName, int projectId, String publicDatasetId, List<String> partners, Map<String, String> fileTopics) {
        this.destinationDatasetName = destinationDatasetName;
        this.projectId = projectId;
        this.publicDatasetId = publicDatasetId;
        this.partners = partners;
        this.fileTopics = fileTopics;
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

    public Map<String, String> getFileTopics() {
        return fileTopics;
    }

    public void setFileTopics(Map<String, String> fileTopics) {
        this.fileTopics = fileTopics;
    }
    
    
    
}
