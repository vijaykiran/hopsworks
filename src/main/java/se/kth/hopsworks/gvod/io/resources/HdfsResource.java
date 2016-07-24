/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.resources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class HdfsResource {
    
    private String hdfsXMLPath;
    private String user;

    public HdfsResource(String hdfsXMLPath, String user) {
        this.hdfsXMLPath = hdfsXMLPath;
        this.user = user;
    }

    public void setHdfsXMLPath(String hdfsXMLPath) {
        this.hdfsXMLPath = hdfsXMLPath;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHdfsXMLPath() {
        return hdfsXMLPath;
    }

    public String getUser() {
        return user;
    }
    
    
    
    
    
}
