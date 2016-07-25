/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.requestmanifest;

import se.kth.hopsworks.gvod.io.resources.HdfsResource;

/**
 *
 * @author jsvhqr
 */
public class DownloadRequest {
    
    
    private HdfsResource hdfsResource;
    private String publicDatasetId;
    private String datasetPath;

    public DownloadRequest() {
    }

    public DownloadRequest(HdfsResource hdfsResource, String publicDatasetId, String datasetPath) {
        this.hdfsResource = hdfsResource;
        this.publicDatasetId = publicDatasetId;
        this.datasetPath = datasetPath;
    }

    public HdfsResource getHdfsResource() {
        return hdfsResource;
    }

    public void setHdfsResource(HdfsResource hdfsResource) {
        this.hdfsResource = hdfsResource;
    }

    public String getPublicDatasetId() {
        return publicDatasetId;
    }

    public void setPublicDatasetId(String publicDatasetId) {
        this.publicDatasetId = publicDatasetId;
    }

    public String getDatasetPath() {
        return datasetPath;
    }

    public void setDatasetPath(String datasetPath) {
        this.datasetPath = datasetPath;
    }
    
    
    
}
