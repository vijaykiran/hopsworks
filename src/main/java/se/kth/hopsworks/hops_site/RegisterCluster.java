/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

/**
 *
 * @author gnomer_fedora
 */
@Stateful
public class RegisterCluster {

    private final String BASE_URI = "http://bbc1.sics.se:14003/hops-site/webresources";
    private final String cluster_name = "hops";
    private String my_endpoint;
    private final String cluster_email = "johsn@kth.se";
    private final String certificate = "xyz";
    private final String udpendpoint = "udp://bbc1.sics.se:14003/hops-site/webapi/myresource";
    private Client client;
    private JSONObject registeredclusters;
    @Schedule(minute = "*/1", hour = "*", persistent = false)
    public void automaticTimeout() {
        ServletContext servletContext = (ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext();

        my_endpoint = servletContext.getContextPath() + "/api/elastic/publicsearch/";

        client = ClientBuilder.newClient();

        WebTarget target = client.target(BASE_URI).path("myresource");

        String response = this.Ping(MediaType.APPLICATION_JSON.getClass(), cluster_name, my_endpoint, cluster_email, certificate, udpendpoint,target);
        
        registeredclusters = new JSONObject(response);
        
    }
    
    private <T> T Ping(Class<T> responseType, String name, String restEndpoint, String email, String cert, String udpEndpoint , WebTarget target) throws ClientErrorException {
        WebTarget resource = target;
        resource = resource.path(java.text.MessageFormat.format("ping/{0}/{1}/{2}/{3}/{4}", new Object[]{name, restEndpoint, email, cert, udpEndpoint}));
        return resource.request(MediaType.APPLICATION_JSON).get(responseType);
    }
    
    public JSONObject getRegisteredClusters(){
        return this.registeredclusters;
    }

}
