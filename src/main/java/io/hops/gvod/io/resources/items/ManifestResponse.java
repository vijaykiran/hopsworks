/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.resources.items;

import javax.ws.rs.core.Response;

/**
 *
 * @author jsvhqr
 */
public class ManifestResponse {
    
    private ManifestJson manifest;
    private Response response;

    public ManifestResponse(ManifestJson manifest, Response response) {
        this.manifest = manifest;
        this.response = response;
    }

    public ManifestJson getManifest() {
        return manifest;
    }

    public void setManifest(ManifestJson manifest) {
        this.manifest = manifest;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
    
    
    
}
