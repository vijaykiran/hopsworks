/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.download;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DownloadRequest {
    
    private String datasetName;
    
    private String publicDatasetId;
    
    private int projectId;

    public DownloadRequest() {
    }

    public DownloadRequest(String datasetName, String publicDatasetId, int projectId) {
        this.datasetName = datasetName;
        this.publicDatasetId = publicDatasetId;
        this.projectId = projectId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
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

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    
    
    
}
