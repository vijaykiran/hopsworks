/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.stop;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class RemoveTorrentDTO {
    
    private String torrentId;

    public RemoveTorrentDTO() {
    }
    
    public String getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(String torrentId) {
        this.torrentId = torrentId;
    }
    
    
    
}
