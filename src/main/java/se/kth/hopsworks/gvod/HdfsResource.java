/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod;

/**
 *
 * @author jsvhqr
 */
public class HdfsResource {
    
    private final String hopsIp;
    private final String hopsPort;
    private final String dirPath;
    private final String fileName;
    private final String user;

    public HdfsResource(String hopsIp, String hopsPort, String dirPath, String fileName, String user) {
        this.hopsIp = hopsIp;
        this.hopsPort = hopsPort;
        this.dirPath = dirPath;
        this.fileName = fileName;
        this.user = user;
    }
    
    
    
}
