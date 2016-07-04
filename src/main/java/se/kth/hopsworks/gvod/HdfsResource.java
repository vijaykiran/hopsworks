/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class HdfsResource {
    
    private final String hdfsConfigurationXML;
    private final String dirPath;
    private final String fileName;
    private final String user;

    public HdfsResource(String hdfsConfigurationXML, String dirPath, String fileName, String user) {
        this.hdfsConfigurationXML = hdfsConfigurationXML;
        this.dirPath = dirPath;
        this.fileName = fileName;
        this.user = user;
    }

    public String getHdfsConfigurationXML() {
        return hdfsConfigurationXML;
    }
    
    public String getDirPath() {
        return dirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUser() {
        return user;
    }
    
    
    
}
