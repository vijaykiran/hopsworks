/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.resources;

import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.gvod.io.resources.items.ManifestJson;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class HdfsResource {
    
    private String hdfsXMLPath;
    private String dirPath;
    private ManifestJson manifestJson;
    private String user;

    public HdfsResource(String hdfsXMLPath, String dirPath, ManifestJson manifestJson, String user) {
        this.hdfsXMLPath = hdfsXMLPath;
        this.dirPath = dirPath;
        this.manifestJson = manifestJson;
        this.user = user;
    }

    public void setHdfsXMLPath(String hdfsXMLPath) {
        this.hdfsXMLPath = hdfsXMLPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public void setManifestJson(ManifestJson manifestJson) {
        this.manifestJson = manifestJson;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHdfsXMLPath() {
        return hdfsXMLPath;
    }
    
    
    public String getDirPath() {
        return dirPath;
    }

    public String getUser() {
        return user;
    }

    public ManifestJson getManifestJson() {
        return manifestJson;
    }
    
    
    
}
