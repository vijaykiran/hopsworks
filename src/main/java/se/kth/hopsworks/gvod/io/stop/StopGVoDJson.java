/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.stop;

import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.gvod.io.resources.items.TorrentId;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class StopGVoDJson {
    
      private TorrentId torrentId;
      private String manifestJsonPath;

    public StopGVoDJson() {
    }

    public StopGVoDJson(String manifestJsonPath, TorrentId torrentId) {
        this.torrentId = torrentId;
        this.manifestJsonPath = manifestJsonPath;
    }

    public TorrentId getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(TorrentId torrentId) {
        this.torrentId = torrentId;
    }

    public String getManifestJsonPath() {
        return manifestJsonPath;
    }

    public void setManifestJsonPath(String manifestJsonPath) {
        this.manifestJsonPath = manifestJsonPath;
    }
      
      
    
    
}
