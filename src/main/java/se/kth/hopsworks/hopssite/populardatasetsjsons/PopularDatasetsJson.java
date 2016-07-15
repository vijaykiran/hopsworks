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
public class PopularDatasetsJson {
    
    private String datasetName;
    
    private DatasetStructure structure;
    
    private String datasetId;
    
    private int files;
    
    private int leeches;
    
    private int seeds;

    public PopularDatasetsJson(String datasetName, DatasetStructure structure, String datasetId, int files, int leeches, int seeds) {
        this.datasetName = datasetName;
        this.structure = structure;
        this.datasetId = datasetId;
        this.files = files;
        this.leeches = leeches;
        this.seeds = seeds;
    }
    

    public PopularDatasetsJson() {
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public void setStructure(DatasetStructure structure) {
        this.structure = structure;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public void setLeeches(int leeches) {
        this.leeches = leeches;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public DatasetStructure getStructure() {
        return structure;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public int getFiles() {
        return files;
    }

    public int getLeeches() {
        return leeches;
    }

    public int getSeeds() {
        return seeds;
    }
}