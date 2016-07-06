/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDatasetsJson {
    
    private final String datasetName;
    
    private final String structure;
    
    private final String datasetId;
    
    private final int files;
    
    private final int leeches;
    
    private final int seeds;

    PopularDatasetsJson(String name, String datasetStructure, String publicDsId, int files, int leeches, int seeds) {
        
        this.datasetName = name;
        this.structure = datasetStructure;
        this.datasetId = publicDsId;
        this.files = files;
        this.leeches = leeches;
        this.seeds = seeds;
        
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getStructure() {
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