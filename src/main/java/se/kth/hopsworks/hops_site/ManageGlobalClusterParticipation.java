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
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import org.json.JSONArray;
import se.kth.hopsworks.util.Settings;

@Startup
@Singleton
public class ManageGlobalClusterParticipation {

    private JSONArray registeredClusters = null;
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
        if (settings.getCLUSTER_ID() == null) {
            TryToRegister();
        } else {
            DoTimeout();
        }
    }

    @Timeout
    public void TimeoutOcurred() {

        if (settings.getCLUSTER_ID() != null) {
            TryToPing();
        } else {
            TryToRegister();
        }
    }

    public void TryToRegister() {

        String id = null;
        try {
            id = RegisterRest(String.class, settings.getELASTIC_PUBLIC_RESTENDPOINT(), settings.getCLUSTER_MAIL(), settings.getCLUSTER_CERT(), settings.getGVOD_UDP_ENDPOINT());
        } catch (ClientErrorException ex) {

        } finally {
            if (id != null) {
                settings.setCLUSTER_ID(id);
            }
            DoTimeout();
        }

    }

    public void TryToPing() {

        String response = null;
        try {
            response = PingRest(String.class, settings.getCLUSTER_ID(), settings.getELASTIC_PUBLIC_RESTENDPOINT(), settings.getCLUSTER_MAIL(), settings.getCLUSTER_CERT(), settings.getGVOD_UDP_ENDPOINT());
        } catch (ClientErrorException ex) {

        } finally {
            if (response != null) {
                this.registeredClusters = new JSONArray(response);
            }
            DoTimeout();
        }

    }

    public <T> T PingRest(Class<T> responseType, String clusterId, String searchEndpoint, String email, String cert, String gvodEndpoint) throws ClientErrorException {
        WebTarget resource = webTarget;
        searchEndpoint = searchEndpoint.replaceAll("/", "'");
        gvodEndpoint = gvodEndpoint.replaceAll("/", "'");
        resource = resource.path(java.text.MessageFormat.format("ping/{0}/{1}/{2}/{3}/{4}", new Object[]{clusterId, searchEndpoint, email, cert, gvodEndpoint}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T RegisterRest(Class<T> responseType, String searchEndpoint, String email, String cert, String gvodEndpoint) throws ClientErrorException {
        WebTarget resource = webTarget;
        searchEndpoint = searchEndpoint.replaceAll("/", "'");
        gvodEndpoint = gvodEndpoint.replaceAll("/", "'");
        resource = resource.path(java.text.MessageFormat.format("register/{0}/{1}/{2}/{3}", new Object[]{searchEndpoint, email, cert, gvodEndpoint}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public JSONArray getRegisteredClusters() {
        return this.registeredClusters;
    }

    private void DoTimeout() {
        
        long duration = 6000;
        TimerConfig timerConfig = new TimerConfig(); 
        timerConfig.setPersistent(false);
        Timer timer = context.getTimerService().createSingleActionTimer(duration, timerConfig);

    }

}
