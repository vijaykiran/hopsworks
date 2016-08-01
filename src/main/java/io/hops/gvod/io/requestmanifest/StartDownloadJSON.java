/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.requestmanifest;

import io.hops.gvod.io.resources.HDFSEndpoint;
import io.hops.gvod.io.resources.HDFSResource;
import io.hops.gvod.io.resources.items.AddressJSON;
import io.hops.gvod.io.resources.items.TorrentId;
import java.util.List;

/**
 *
 * @author jsvhqr
 */
public class StartDownloadJSON {
    
    
    private TorrentId torrentId;
    private HDFSResource manifestHDFSResource;
    private List<AddressJSON> partners;
    private HDFSEndpoint hdfsEndpoint;

    public StartDownloadJSON(TorrentId torrentId, HDFSResource manifestHDFSResource, List<AddressJSON> partners, HDFSEndpoint hdfsEndpoint) {
        this.torrentId = torrentId;
        this.manifestHDFSResource = manifestHDFSResource;
        this.partners = partners;
        this.hdfsEndpoint = hdfsEndpoint;
    }

    
    
    public StartDownloadJSON() {
    }

    public TorrentId getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(TorrentId torrentId) {
        this.torrentId = torrentId;
    }

    public HDFSResource getManifestHDFSResource() {
        return manifestHDFSResource;
    }

    public void setManifestHDFSResource(HDFSResource manifestHDFSResource) {
        this.manifestHDFSResource = manifestHDFSResource;
    }

    public List<AddressJSON> getPartners() {
        return partners;
    }

    public void setPartners(List<AddressJSON> partners) {
        this.partners = partners;
    }

    public HDFSEndpoint getHdfsEndpoint() {
        return hdfsEndpoint;
    }

    public void setHdfsEndpoint(HDFSEndpoint hdfsEndpoint) {
        this.hdfsEndpoint = hdfsEndpoint;
    }
    
    
    
}
