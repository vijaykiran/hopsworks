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
public class GVodUploadInput {
    
    private final HdfsResource hdfsResource;
    private final TorrentId torrentId;

    public GVodUploadInput(HdfsResource resource, TorrentId torrentId) {
        this.hdfsResource = resource;
        this.torrentId = torrentId;
    }   
    
    
    
}
