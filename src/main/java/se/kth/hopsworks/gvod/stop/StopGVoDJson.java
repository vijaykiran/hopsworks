/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.stop;

import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.gvod.resources.items.TorrentId;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class StopGVoDJson {
    
      private String fileName;
      private TorrentId torrentId;

    public StopGVoDJson() {
    }

    public StopGVoDJson(String fileName, TorrentId torrentId) {
        this.fileName = fileName;
        this.torrentId = torrentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public TorrentId getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(TorrentId torrentId) {
        this.torrentId = torrentId;
    }
      
      
    
    
}
