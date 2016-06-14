/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import org.json.JSONArray;
import se.kth.hopsworks.util.Settings;

@Startup
@DependsOn("Settings")
@Singleton
public class ManageGlobalClusterParticipation {

    private JSONArray registeredClusters = null;
    private JSONArray popularDatasets = null;
    private WebTarget webTarget;
    private Client client;
    
    @Resource
    private SessionContext context;

    @EJB
    private Settings settings;

    @PostConstruct
    private void init() {

        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(settings.getBASE_URI_HOPS_SITE()).path("myresource");
        if (settings.getCLUSTER_ID() == null) {
            TryToRegister();
        } else {
            DoTimeout();
        }
    }

    @Timeout
    private void TimeoutOcurred() {
        if (settings.getCLUSTER_ID() != null) {
            PingAndGetPopularDatasets();
        } else {
            TryToRegister();
        }
    }

    private void TryToRegister() {

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

    private void PingAndGetPopularDatasets() {

        String pingResponse = null;
        String popularDatasetsResponse = null;
        try {
            pingResponse = PingRest(String.class, settings.getCLUSTER_ID());
            popularDatasetsResponse = PopularDatasetsRest(String.class, settings.getCLUSTER_ID());
        } catch (ClientErrorException ex) {

        } finally {
            if (pingResponse != null) {
                this.registeredClusters = new JSONArray(pingResponse);
            }
            if(popularDatasetsResponse != null){
                this.popularDatasets = new JSONArray(popularDatasetsResponse);
            }
            DoTimeout();
        }

    }

    private <T> T PingRest(Class<T> responseType, String clusterId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ping/{0}/", new Object[]{clusterId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
    
    private <T> T PopularDatasetsRest(Class<T> responseType, String clusterId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("populardatasets/{0}/", new Object[]{clusterId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
    

    private <T> T RegisterRest(Class<T> responseType, String searchEndpoint, String email, String cert, String gvodEndpoint) throws ClientErrorException {
        WebTarget resource = webTarget;
        searchEndpoint = searchEndpoint.replaceAll("/", "'");
        gvodEndpoint = gvodEndpoint.replaceAll("/", "'");
        resource = resource.path(java.text.MessageFormat.format("register/{0}/{1}/{2}/{3}", new Object[]{searchEndpoint, email, cert, gvodEndpoint}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public JSONArray getRegisteredClusters() {
        return this.registeredClusters;
    }
    
    public JSONArray getPopularDatasets() {
        return this.popularDatasets;
    }

    private void DoTimeout() {
        
        long duration = 60000;
        TimerConfig timerConfig = new TimerConfig(); 
        timerConfig.setPersistent(false);
        context.getTimerService().createSingleActionTimer(duration, timerConfig);

    }

}
