/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.downloadHdfsKafka;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.dataset.DatasetStructure;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DownloadHdfsKafkaJson {
    
    
    private int projectId;
    
    private DatasetStructure datasetStructure;
    
    private String datasetId;
    
    private List<String> partners;
    
    private String topicName;

    public DownloadHdfsKafkaJson() {
    }

    public int getProjectId() {
        return projectId;
    }

    public DatasetStructure getDatasetStructure() {
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
