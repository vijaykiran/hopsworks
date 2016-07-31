/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.hopssite.restCommunication;

import io.hops.hopssite.io.register.RegisterJson;
import io.hops.hopssite.io.populardatasets.PopularDatasetJson;
import java.util.List;
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
import javax.ejb.TimerConfig;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.hops.gvod.io.resources.items.ManifestJson;
import io.hops.hopssite.io.identity.IdentificationJson;
import io.hops.hopssite.io.ping.PingedJson;
import io.hops.hopssite.io.register.RegisteredJson;
import io.hops.hopssite.io.register.RegisteredClusterJson;
import se.kth.hopsworks.util.Settings;

@Startup
@Singleton
public class ManageGlobalClusterParticipation {

    private List<RegisteredClusterJson> registeredClusters = null;
    private List<PopularDatasetJson> popularDatasets = null;
    private WebTarget webTarget = null;
    private Client client = null;

    @Resource
    private SessionContext context;

    @EJB
    private Settings settings;

    @PostConstruct
    private void init() {
        DoTimeout();
    }

    @Timeout
    private void TimeoutOcurred() {

        if (webTarget != null && client != null) {
            doTimeoutStuff();
        } else {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(settings.getBASE_URI_HOPS_SITE()).path("myresource");
            doTimeoutStuff();
        }

    }

    private void doTimeoutStuff() {
        if (settings.getCLUSTER_ID() != null) {
            PingAndGetPopularDatasets();
        } else {
            TryToRegister();
        }
    }

    private void TryToRegister() {

        String id = null;
        try {
            String gvodEndpoint = settings.getGVOD_UDP_ENDPOINT();
            if (gvodEndpoint != null) {
                id = RegisterRest(settings.getELASTIC_PUBLIC_RESTENDPOINT(), settings.getCLUSTER_MAIL(), settings.getCLUSTER_CERT(), gvodEndpoint);
            }
        } catch (ClientErrorException ex) {

        } finally {
            if (id != null) {
                settings.setCLUSTER_ID(id);
            }
            DoTimeout();
        }

    }

    private void PingAndGetPopularDatasets() {

        List<RegisteredClusterJson> pingResponse = null;
        List<PopularDatasetJson> popularDatasetsResponse = null;
        try {
            pingResponse = PingRest(settings.getCLUSTER_ID());
            popularDatasetsResponse = getPopularDatasets(settings.getCLUSTER_ID());
        } catch (ClientErrorException ex) {

        } finally {
            if (pingResponse != null) {
                this.registeredClusters = pingResponse;
            }
            if (popularDatasetsResponse != null) {
                this.popularDatasets = popularDatasetsResponse;
            }
            DoTimeout();
        }

    }

    private List<RegisteredClusterJson> PingRest(String clusterId) {
        WebTarget resource = webTarget.path("ping");

        Response r = resource.request().accept(MediaType.APPLICATION_JSON).put(Entity.json(new IdentificationJson(clusterId)));

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(PingedJson.class).getClusters();
        } else {
            return null;
        }
    }

    private List<PopularDatasetJson> getPopularDatasets(String clusterId){

        WebTarget resource = webTarget.path("populardatasets");

        Response r = resource.request().accept(MediaType.APPLICATION_JSON).put(Entity.json(new IdentificationJson(clusterId)));

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(new GenericType<List<PopularDatasetJson>>(){});
        } else {
            return null;
        }
    }

    private String RegisterRest(String searchEndpoint, String email, String cert, String gvodEndpoint) {

        WebTarget resource = webTarget.path("register");
        
        Response r = resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(new RegisterJson(searchEndpoint, gvodEndpoint, email, cert)));

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(RegisteredJson.class).getClusterId();
        } else {
            return null;
        }

    }

    private void DoTimeout() {

        long duration = 60000;
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        context.getTimerService().createSingleActionTimer(duration, timerConfig);

    }

    public void notifyHopsSiteAboutNewDataset(ManifestJson manifestJson, String publicDsId,int leeches, int seeds) {
        
        WebTarget resource = webTarget.path("populardatasets");
        
        resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(new PopularDatasetJson(manifestJson, publicDsId,leeches, seeds)));
        
        
    }

    public List<RegisteredClusterJson> getRegisteredClusters() {
        return registeredClusters;
    }

    public List<PopularDatasetJson> getPopularDatasets() {
        return popularDatasets;
    }
    
    

}
