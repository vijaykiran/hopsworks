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
    
    private HdfsResource hdfsResource;
    private HopsResource hopsResource;
    private TorrentId torrentId;
    private String datasetPath;

    public UploadGVoDJson(HdfsResource hdfsResource,HopsResource hopsResource , TorrentId torrentId, String datasetPath) {
        this.hdfsResource = hdfsResource;
        this.hopsResource = hopsResource;
        this.torrentId = torrentId;
        this.datasetPath = datasetPath;
    }  

    public UploadGVoDJson() {
    }

    public HdfsResource getHdfsResource() {
        return hdfsResource;
    }

    public void setHdfsResource(HdfsResource hdfsResource) {
        this.hdfsResource = hdfsResource;
    }

    public HopsResource getHopsResource() {
        return hopsResource;
    }

    public void setHopsResource(HopsResource hopsResource) {
        this.hopsResource = hopsResource;
    }

    public TorrentId getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(TorrentId torrentId) {
        this.torrentId = torrentId;
    }

    public String getDatasetPath() {
        return datasetPath;
    }

    public void setDatasetPath(String datasetPath) {
        this.datasetPath = datasetPath;
    }
    
    
    
}
