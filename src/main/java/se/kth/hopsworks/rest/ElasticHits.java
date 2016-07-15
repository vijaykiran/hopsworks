/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.rest;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class ElasticHits {
    
    private List<ElasticHit> elasticHits;

    public ElasticHits() {
    }

    ElasticHits(List<ElasticHit> elasticHits) {
        this.elasticHits = elasticHits;
    }

    public List<ElasticHit> getElasticHits() {
        return elasticHits;
    }

    public void setElasticHits(List<ElasticHit> elasticHits) {
        this.elasticHits = elasticHits;
    }
    
    
    
}
