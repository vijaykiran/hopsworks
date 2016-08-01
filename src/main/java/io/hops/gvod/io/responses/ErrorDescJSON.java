/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.responses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class ErrorDescJSON {
    private String details;
    
    public ErrorDescJSON(String details) {
        this.details = details;
    }
    
    public ErrorDescJSON() {}

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}