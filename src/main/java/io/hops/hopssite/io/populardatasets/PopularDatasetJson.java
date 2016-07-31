/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.hopssite.io.populardatasets;

import javax.xml.bind.annotation.XmlRootElement;
import io.hops.gvod.io.resources.items.ManifestJson;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDatasetJson {
    
    private ManifestJson manifestJson;
    
    private String datasetId;
    
    private int leeches;
    
    private int seeds;

    public PopularDatasetJson(ManifestJson manifestJson, String datasetId, int leeches, int seeds) {
        this.manifestJson = manifestJson;
        this.datasetId = datasetId;
        this.leeches = leeches;
        this.seeds = seeds;
    }
    

    public PopularDatasetJson() {
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public void setLeeches(int leeches) {
        this.leeches = leeches;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    public ManifestJson getManifestJson() {
        return manifestJson;
    }

    public void setManifestJson(ManifestJson manifestJson) {
        this.manifestJson = manifestJson;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public int getLeeches() {
        return leeches;
    }

    public int getSeeds() {
        return seeds;
    }
    
    
}