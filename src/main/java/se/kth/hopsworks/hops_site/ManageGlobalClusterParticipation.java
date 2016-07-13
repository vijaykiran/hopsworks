/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import se.kth.hopsworks.hops_site.register.RegisterJson;
import se.kth.hopsworks.hops_site.popular_datasets.AddPopularDatasetJson;
import com.owlike.genson.Genson;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import se.kth.hopsworks.dataset.DatasetStructure;
import se.kth.hopsworks.hops_site.ping.PingJson;
import se.kth.hopsworks.hops_site.popular_datasets.GetPopularDatasetsJson;
import se.kth.hopsworks.util.Settings;

@Startup
@Singleton
public class ManageGlobalClusterParticipation {

    private JSONArray registeredClusters = null;
    private JSONArray popularDatasets = null;
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

        String pingResponse = null;
        String popularDatasetsResponse = null;
        try {
            pingResponse = PingRest(settings.getCLUSTER_ID());
            popularDatasetsResponse = getPopularDatasets(settings.getCLUSTER_ID());
        } catch (ClientErrorException ex) {

        } finally {
            if (pingResponse != null) {
                this.registeredClusters = new JSONArray(pingResponse);
            }
            if (popularDatasetsResponse != null) {
                this.popularDatasets = new JSONArray(popularDatasetsResponse);
            }
            DoTimeout();
        }

    }

    private String PingRest(String clusterId) {
        
        PingJson pingJson = new PingJson(clusterId);

        WebTarget resource = webTarget.path("ping");

        Response r = resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(pingJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    private String getPopularDatasets(String clusterId){
        
        GetPopularDatasetsJson getPopularDatasetsJson = new GetPopularDatasetsJson(clusterId);

        WebTarget resource = webTarget.path("populardatasets");

        Response r = resource.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(getPopularDatasetsJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    private String RegisterRest(String searchEndpoint, String email, String cert, String gvodEndpoint) {

        RegisterJson registerJson = new RegisterJson(searchEndpoint, gvodEndpoint, email, cert);

        WebTarget resource = webTarget.path("register");

        Response r = resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(registerJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }

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

    public void notifyHopsSiteAboutNewDataset(String name, DatasetStructure datasetStructure, String publicDsId, int size, int leeches, int seeds) {
        
        AddPopularDatasetJson addPopularDatasetJson = new AddPopularDatasetJson(name, datasetStructure, publicDsId, size,leeches, seeds);
        
        WebTarget resource = webTarget.path("populardatasets");
        
        Response r = resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(addPopularDatasetJson, MediaType.APPLICATION_JSON), Response.class);
        
        
    }

}
