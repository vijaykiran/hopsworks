/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.upload;

import se.kth.hopsworks.gvod.io.resources.items.TorrentId;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.gvod.io.resources.HdfsResource;
import se.kth.hopsworks.gvod.io.resources.HopsResource;


/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class UploadGVoDJson {
    
    private final HdfsResource hdfsResource;
    private final HopsResource hopsResource;
    private final TorrentId torrentId;

    public UploadGVoDJson(HdfsResource hdfsResource,HopsResource hopsResource , TorrentId torrentId) {
        this.hdfsResource = hdfsResource;
        this.hopsResource = hopsResource;
        this.torrentId = torrentId;
    }   

    public TorrentId getTorrentId() {
        return torrentId;
    }

    public HdfsResource getHdfsResource() {
        return hdfsResource;
    }

    public HopsResource getHopsResource() {
        return hopsResource;
    }
    
    
    
}
