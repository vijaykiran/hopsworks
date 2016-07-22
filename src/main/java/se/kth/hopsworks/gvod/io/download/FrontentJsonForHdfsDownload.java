/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.download;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.gvod.io.resources.items.ManifestJson;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class FrontentJsonForHdfsDownload {
    
    private int projectId;
    
    private ManifestJson manifestJson;
    
    private String datasetId;
    
    private List<String> partners;

    public FrontentJsonForHdfsDownload() {
    }

    public int getProjectId() {
        return projectId;
    }

    public String getDatasetId() {
        return datasetId;
    }
    
    @XmlElement(name = "partners")
    public List<String> getPartners() {
        return partners;
    }

    public ManifestJson getManifestJson() {
        return manifestJson;
    }

    public void setManifestJson(ManifestJson manifestJson) {
        this.manifestJson = manifestJson;
    }
    
    
    
    
    
}
