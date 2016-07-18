/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hopssite.populardatasetsjsons;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class SuccessGetPopularDatasetsJson {
    
    private List<PopularDatasets> popularDatasets;

    public SuccessGetPopularDatasetsJson() {
    }

    public List<PopularDatasets> getPopularDatasets() {
        return popularDatasets;
    }

    public void setPopularDatasets(List<PopularDatasets> popularDatasets) {
        this.popularDatasets = popularDatasets;
    }
    
    
    
}
