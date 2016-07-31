/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.download;

import io.hops.gvod.io.resources.items.AddressJSON;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DownloadRequest {
    
    private String datasetName;
    
    private String datasetId;
    
    private int projectId;
    
    private List<AddressJSON> partners;

    public DownloadRequest() {}

    public DownloadRequest(String datasetName, String datasetId, int projectId, List<AddressJSON> partners) {
        this.datasetName = datasetName;
        this.datasetId = datasetId;
        this.projectId = projectId;
        this.partners = partners;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public List<AddressJSON> getPartners() {
        return partners;
    }

    public void setPartners(List<AddressJSON> partners) {
        this.partners = partners;
    }
    
    
    
}
