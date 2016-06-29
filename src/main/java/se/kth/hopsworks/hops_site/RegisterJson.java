/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class RegisterJson {

    private String searchEndpoint;

    private String gvodEndpoint;

    private String email;

    private String cert;

    public RegisterJson(String searchEndpoint, String gvodEndpoint, String email, String cert) {
        this.searchEndpoint = searchEndpoint;
        this.gvodEndpoint = gvodEndpoint;
        this.email = email;
        this.cert = cert;
    }

    public String getSearchEndpoint() {
        return searchEndpoint;
    }

    public String getGVodEndpoint() {
        return gvodEndpoint;
    }

    public String getEmail() {
        return email;
    }

    public String getCert() {
        return cert;
    }

}
