/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.download;

import io.hops.gvod.io.resources.KafkaResource;
import io.hops.gvod.io.resources.items.ExtendedDetails;
import io.hops.gvod.io.resources.items.TorrentId;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DownloadGVoDJson {

    private TorrentId torrentId;
    private KafkaResource kafkaEndpoint;
    private ExtendedDetails extendedDetails;

    public DownloadGVoDJson() {
    }
    
    public DownloadGVoDJson(TorrentId torrentId, KafkaResource kafkaEndpoint, ExtendedDetails extendedDetails) {
        this.torrentId = torrentId;
        this.kafkaEndpoint = kafkaEndpoint;
        this.extendedDetails = extendedDetails;
    }

    public TorrentId getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(TorrentId torrentId) {
        this.torrentId = torrentId;
    }

    public KafkaResource getKafkaEndpoint() {
        return kafkaEndpoint;
    }

    public void setKafkaEndpoint(KafkaResource kafkaEndpoint) {
        this.kafkaEndpoint = kafkaEndpoint;
    }

    public ExtendedDetails getExtendedDetails() {
        return extendedDetails;
    }

    public void setExtendedDetails(ExtendedDetails extendedDetails) {
        this.extendedDetails = extendedDetails;
    }
    
    
    
    
}
