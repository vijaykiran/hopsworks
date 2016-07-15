/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hopssite.populardatasetsjsons;

import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.dataset.DatasetStructure;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDatasets {
    
    private String name;
    
    private String datasetId;
    
    private int files;
    
    private int leeches;
    
    private int seeds;
    
    private DatasetStructure datasetStructure;

    public PopularDatasets(String name, String datasetId, int files, int leeches, int seeds, DatasetStructure datasetStructure) {
        this.name = name;
        this.datasetId = datasetId;
        this.files = files;
        this.leeches = leeches;
        this.seeds = seeds;
        this.datasetStructure = datasetStructure;
    }

    public PopularDatasets() {
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public int getLeeches() {
        return leeches;
    }

    public void setLeeches(int leeches) {
        this.leeches = leeches;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    public DatasetStructure getDatasetStructure() {
        return datasetStructure;
    }

    public void setDatasetStructure(DatasetStructure datasetStructure) {
        this.datasetStructure = datasetStructure;
    }
    
    
    
}
