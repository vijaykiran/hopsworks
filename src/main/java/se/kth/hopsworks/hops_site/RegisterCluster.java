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

    @Resource
    private SessionContext context;
    
    @EJB
    private Settings settings;

    @PostConstruct
    public void init() {

        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(settings.getBASE_URI_HOPS_SITE()).path("myresource");
        TryToRegister();
        if (settings.getCLUSTER_ID() != null) {
            TryToPing();
            if (this.registeredclusters != null) {
                DoTimeout();
            }
        } else {
            DoTimeout();
        }

    }

    @Timeout
    public void TimeoutOcurred() {

        if (settings.getCLUSTER_ID() != null) {
            String response = this.PingRest(String.class, settings.getCLUSTER_ID(), settings.getELASTIC_PUBLIC_RESTENDPOINT(), settings.getCLUSTER_MAIL(), settings.getCLUSTER_CERT(), settings.getGVOD_UDP_ENDPOINT());

            if (response != null) {
                registeredclusters = new JSONArray(response);
            }

            DoTimeout();
        } else {
            TryToRegister();
            if (settings.getCLUSTER_ID() != null) {
                TryToPing();
                if (this.registeredclusters != null) {
                    DoTimeout();
                }
            } else {
                DoTimeout();
            }
        }

    }

    public void TryToRegister() {

        String id = null;
        try {
            id = RegisterRest(String.class, settings.getELASTIC_PUBLIC_RESTENDPOINT(), settings.getCLUSTER_MAIL(), settings.getCLUSTER_CERT(), settings.getGVOD_UDP_ENDPOINT());
        } catch (ClientErrorException ex) {

        } finally {
            if (settings.getCLUSTER_ID() != null) {
                settings.setCLUSTER_ID(id);
            }
        }

    }

    public void TryToPing() {

        String response = null;
        try {
            response = PingRest(String.class, settings.getCLUSTER_ID(), settings.getELASTIC_PUBLIC_RESTENDPOINT(), settings.getCLUSTER_MAIL(), settings.getCLUSTER_CERT(), settings.getGVOD_UDP_ENDPOINT());
        } catch (ClientErrorException ex) {

        } finally {
            if (settings.getCLUSTER_ID() != null) {
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

    private void DoTimeout() {
        context.getTimerService().createTimer(60000, "time to ping");
    }

}
