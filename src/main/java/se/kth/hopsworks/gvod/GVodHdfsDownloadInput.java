/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod;

import org.json.JSONArray;

/**
 *
 * @author jsvhqr
 */
public class GVodHdfsDownloadInput {

    private final HdfsResource resource;
    private final TorrentId torrentId;
    private final JSONArray partners;

    public GVodHdfsDownloadInput(HdfsResource resource, TorrentId torrentId, JSONArray partners) {
        this.resource = resource;
        this.torrentId = torrentId;
        this.partners = partners;
    }
    
    
    
    
    
}
