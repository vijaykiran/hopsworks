/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import org.json.JSONArray;
import org.mortbay.util.ajax.JSON;
import se.kth.hopsworks.util.Settings;

/**
 *
 * @author gnomer_fedora
 */
@Startup
@Singleton
public class RegisterCluster {

    private JSONArray registeredclusters = null;
    private WebTarget webTarget;
    private Client client;
    private String cluster_id;

    @Resource
    private SessionContext context;

    @PostConstruct
    public void init() {

        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(Settings.getBASE_URI_HOPS_SITE()).path("myresource");
        TryToRegister();
        if (this.cluster_id != null) {
            TryToPing();
            if (this.registeredclusters != null) {
                doTimeout();
            }
        } else {
            doTimeout();
        }

    }

    @Timeout
    public void TimeoutOcurred() {

        if (cluster_id != null) {
            String response = this.PingRest(String.class, this.cluster_id, Settings.getELASTIC_PUBLIC_RESTENDPOINT(), Settings.getCLUSTER_MAIL(), Settings.getCLUSTER_CERT(), Settings.getGVOD_UDP_ENDPOINT());

            if (response != null) {
                registeredclusters = new JSONArray(response);
            }

            context.getTimerService().createTimer(60000, "time to ping");
        } else {
            TryToRegister();
            if (this.cluster_id != null) {
                TryToPing();
                if (this.registeredclusters != null) {
                    doTimeout();
                }
            } else {
                doTimeout();
            }
        }

    }

    public void TryToRegister() {

        String id = null;
        try {
            id = RegisterRest(String.class, Settings.getELASTIC_PUBLIC_RESTENDPOINT(), Settings.getCLUSTER_MAIL(), Settings.getCLUSTER_CERT(), Settings.getGVOD_UDP_ENDPOINT());
        } catch (ClientErrorException ex) {

        } finally {
            if (cluster_id != null) {
                this.cluster_id = id;
            } else {
                this.cluster_id = null;
            }
        }

    }

    public void TryToPing() {

        String response = null;
        try {
            response = PingRest(String.class, this.cluster_id, Settings.getELASTIC_PUBLIC_RESTENDPOINT(), Settings.getCLUSTER_MAIL(), Settings.getCLUSTER_CERT(), Settings.getGVOD_UDP_ENDPOINT());
        } catch (ClientErrorException ex) {

        } finally {
            if (cluster_id != null) {
                this.registeredclusters = new JSONArray(response);
            } else {
                this.registeredclusters = null;
            }
        }

    }

    public <T> T PingRest(Class<T> responseType, String name, String restEndpoint, String email, String cert, String udpEndpoint) throws ClientErrorException {
        WebTarget resource = webTarget;
        restEndpoint = restEndpoint.replaceAll("/", "'");
        udpEndpoint = udpEndpoint.replaceAll("/", "'");
        resource = resource.path(java.text.MessageFormat.format("ping/{0}/{1}/{2}/{3}/{4}", new Object[]{name, restEndpoint, email, cert, udpEndpoint}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T RegisterRest(Class<T> responseType, String restEndpoint, String email, String cert, String udpEndpoint) throws ClientErrorException {
        WebTarget resource = webTarget;
        restEndpoint = restEndpoint.replaceAll("/", "'");
        udpEndpoint = udpEndpoint.replaceAll("/", "'");
        resource = resource.path(java.text.MessageFormat.format("register/{0}/{1}/{2}/{3}/{4}", new Object[]{restEndpoint, email, cert, udpEndpoint}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public JSONArray getRegisteredClusters() {
        return this.registeredclusters;
    }

    private void doTimeout() {
        context.getTimerService().createTimer(60000, "time to ping");
    }

}
