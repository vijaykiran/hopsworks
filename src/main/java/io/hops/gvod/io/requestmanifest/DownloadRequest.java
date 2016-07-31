/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.requestmanifest;

import io.hops.gvod.io.resources.HDFSResource;
import io.hops.gvod.io.resources.items.AddressJSON;
import io.hops.gvod.io.resources.items.TorrentId;
import java.util.List;

/**
 *
 * @author jsvhqr
 */
public class DownloadRequest {
    
    
    private TorrentId torrentId;
    private HDFSResource manifestHDFSResource;
    private List<AddressJSON> partners;

    public DownloadRequest() {
    }

    public DownloadRequest(TorrentId torrentId, HDFSResource manifestHDFSResource, List<AddressJSON> partners) {
        this.torrentId = torrentId;
        this.manifestHDFSResource = manifestHDFSResource;
        this.partners = partners;
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
    
    
    
}
