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
public class DownloadGVoDJson {

    private final HdfsResource hdfsResource;
    private final KafkaResource kafkaResource;
    private final TorrentId torrentId;
    private final String partners;

    public DownloadGVoDJson(HdfsResource hdfsResource, KafkaResource kafkaResource, TorrentId torrentId, String partners) {
        this.hdfsResource = hdfsResource;
        this.kafkaResource = kafkaResource;        
        this.torrentId = torrentId;
        this.partners = partners;
    }
    
    
    
    
    
}
