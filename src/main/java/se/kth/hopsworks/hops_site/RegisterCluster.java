/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import org.json.JSONArray;

/**
 *
 * @author gnomer_fedora
 */


@Startup
@Singleton
public class RegisterCluster {

    private final String cluster_name = "hops1";
    private String my_endpoint;
    private final String cluster_email = "johsn@kth.se";
    private final String certificate = "xyz";
    private final String udpendpoint = "http://bbc1.sics.se:14003/hops-site/udpendpoint";
    private JSONArray registeredclusters = null;
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://bbc1.sics.se:14003/hops-site/webresources";
    
    @Resource
    private SessionContext context;

    @PostConstruct
    public void init() {
        
        my_endpoint = "http://bbc1.sics.se:14003/hopsworks/api/elastic/publicdatasets/";

        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("myresource");

        String response = Ping(String.class, cluster_name, my_endpoint, cluster_email, certificate, udpendpoint);
        
        if(response != null){
            registeredclusters = new JSONArray(response);
        }
        context.getTimerService().createTimer(60000, "time to ping");
    }

    @Timeout
    public void TimeoutOcurred() {

        String response = this.Ping(String.class, cluster_name, my_endpoint, cluster_email, certificate, udpendpoint);
        
        if(response != null){
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
