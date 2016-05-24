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


    @PostConstruct
    public void init() {

        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(Settings.getBASE_URI_HOPS_SITE()).path("myresource");
        String response = null;
        try {
            response = Ping(String.class, Settings.getCLUSTER_NAME(), Settings.getELASTIC_PUBLIC_RESTENDPOINT(), Settings.getCLUSTER_MAIL(), Settings.getCLUSTER_CERT(), Settings.getGVOD_UDP_ENDPOINT());
        } catch (ClientErrorException ex) {

        } finally {
            if (response != null) {
                registeredclusters = new JSONArray(response);
                context.getTimerService().createTimer(60000, "time to ping");
            } else {
                context.getTimerService().createTimer(300000, "time to ping");
            }
        }

    }

    @Timeout
    public void TimeoutOcurred() {

        String response = this.Ping(String.class, Settings.getCLUSTER_NAME(), Settings.getELASTIC_PUBLIC_RESTENDPOINT(), Settings.getCLUSTER_MAIL(), Settings.getCLUSTER_CERT(), Settings.getGVOD_UDP_ENDPOINT());

        if (response != null) {
            registeredclusters = new JSONArray(response);
        }

        context.getTimerService().createTimer(60000, "time to ping");
    }

    public <T> T Ping(Class<T> responseType, String name, String restEndpoint, String email, String cert, String udpEndpoint) throws ClientErrorException {
        WebTarget resource = webTarget;
        restEndpoint = restEndpoint.replaceAll("/", "'");
        udpEndpoint = udpEndpoint.replaceAll("/", "'");
        resource = resource.path(java.text.MessageFormat.format("ping/{0}/{1}/{2}/{3}/{4}", new Object[]{name, restEndpoint, email, cert, udpEndpoint}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public JSONArray getRegisteredClusters() {
        return this.registeredclusters;
    }

}
