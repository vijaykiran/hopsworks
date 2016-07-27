/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod.io.status;

import javax.xml.bind.annotation.XmlRootElement;
import se.kth.hopsworks.gvod.io.resources.items.TorrentId;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class StatusGVoDJson {
    
    private TorrentId torrentId;
      private String datasetPath;

    public StatusGVoDJson() {
    }

    public StatusGVoDJson(String datasetPath, TorrentId torrentId) {
        this.torrentId = torrentId;
        this.datasetPath = datasetPath;
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
