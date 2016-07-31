/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.resources.items;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class TorrentId {
    
    private final String val;

    public TorrentId(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
    
    
    
    
}