/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.download;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.dataset.DatasetStructureJson;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class FrontendJsonForHdfsKafkaDownload {
    
    
    private int projectId;
    
    private DatasetStructureJson datasetStructure;
    
    private String datasetId;
    
    private List<String> partners;
    
    private String topicName;

    public FrontendJsonForHdfsKafkaDownload() {
    }

    public int getProjectId() {
        return projectId;
    }

    public DatasetStructureJson getDatasetStructure() {
        return datasetStructure;
    }

    public String getDatasetId() {
        return datasetId;
    }
    
    @XmlElement(name = "partner")
    public List<String> getPartners() {
        return partners;
    }

    public String getTopicName() {
        return topicName;
    }
     
    
      
}
