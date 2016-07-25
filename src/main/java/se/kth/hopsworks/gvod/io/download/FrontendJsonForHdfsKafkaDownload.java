/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.download;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class FrontendJsonForHdfsKafkaDownload {
    
    
    private int projectId;
    
    private String datasetId;
    
    private List<String> partners;
    
    private String topicName;
    
    private String datasetName;

    public FrontendJsonForHdfsKafkaDownload() {
    }

    public int getProjectId() {
        return projectId;
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

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
     
    
      
}
