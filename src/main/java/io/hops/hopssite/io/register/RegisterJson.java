/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.hopssite.io.register;

import io.hops.gvod.io.resources.items.AddressJSON;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class RegisterJson {

    private String searchEndpoint;

    private AddressJSON gvodEndpoint;

    private String email;

    private String cert;

    public RegisterJson(String searchEndpoint, AddressJSON gvodEndpoint, String email, String cert) {
        this.searchEndpoint = searchEndpoint;
        this.gvodEndpoint = gvodEndpoint;
        this.email = email;
        this.cert = cert;
    }

    public RegisterJson() {
    }

    public void setSearchEndpoint(String searchEndpoint) {
        this.searchEndpoint = searchEndpoint;
    }

    public void setGvodEndpoint(AddressJSON gvodEndpoint) {
        this.gvodEndpoint = gvodEndpoint;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }
        
    public String getSearchEndpoint() {
        return searchEndpoint;
    }

    public AddressJSON getGvodEndpoint() {
        return gvodEndpoint;
    }

    public String getEmail() {
        return email;
    }

    public String getCert() {
        return cert;
    }

}
