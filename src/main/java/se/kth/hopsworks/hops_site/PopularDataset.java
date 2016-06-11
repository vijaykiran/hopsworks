/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDataset {
    private String name;
    private String datasetId;
    private int files;
    private int size;
    private int leeches;
    private int seeds;
    
    
    public PopularDataset(JSONObject jsonObject){
        
        this.name = jsonObject.getString("name");
        this.datasetId = jsonObject.getString("datasetId");
        this.files = jsonObject.getInt("files");
        this.size = jsonObject.getInt("size");
        this.leeches = jsonObject.getInt("leeches");
        this.seeds = jsonObject.getInt("seeds");
        
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
    
    
    
}
