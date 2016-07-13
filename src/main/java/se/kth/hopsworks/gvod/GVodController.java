/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod;

import com.owlike.genson.Genson;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import se.kth.bbc.project.Project;
import se.kth.bbc.project.ProjectFacade;
import se.kth.hopsworks.controller.DatasetController;
import se.kth.hopsworks.dataset.DatasetStructure;
import se.kth.hopsworks.hdfsUsers.controller.HdfsUsersController;
import se.kth.hopsworks.user.model.Users;
import se.kth.hopsworks.users.UserFacade;
import se.kth.hopsworks.util.Settings;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class GVodController {
    
    @EJB
    Settings settings;
    
    @EJB
    DatasetController datasetController;
    
    @EJB
    ProjectFacade projectFacade;
    
    @EJB
    UserFacade userFacade;
    
    @EJB
    private HdfsUsersController hdfsUsersController;
    
    
    private WebTarget webTarget = null;
    private Client rest_client = null;

    public String uploadToGVod(int projectId,String hdfsConfigXMLPath, String path, DatasetStructure datasetStructure, String username, String publicDsId) {
        
        UploadGVoDJson uploadGVoDJson = new UploadGVoDJson(new HdfsResource(hdfsConfigXMLPath,path,datasetStructure.getChildrenFiles().get(0),username), new HopsResource(projectId), new TorrentId(publicDsId));
        
        rest_client = ClientBuilder.newClient();
        
        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/upload/xml");
        
        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(uploadGVoDJson, MediaType.APPLICATION_JSON), Response.class);
        
        if(r != null && r.getStatus() == 200){
            return r.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    public String downloadHdfs(String hdfsConfigXMLPath, Project project, DatasetStructure datasetStructure, Users user, String publicDsId, List<String> partners) throws IOException  {
        
        String dsPath = datasetController.createDatasetForDownload(user, project, datasetStructure, publicDsId);
        
        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(new HdfsResource(hdfsConfigXMLPath, dsPath,datasetStructure.getChildrenFiles().get(0),hdfsUsersController.getHdfsUserName(project, user)),null, new HopsResource(project.getId()),new TorrentId(publicDsId), partners);
        
        rest_client = ClientBuilder.newClient();
        
        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/download/xml");
        
        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadGVoDJson, MediaType.APPLICATION_JSON), Response.class);
        
        if(r != null && r.getStatus() == 200){
            return r.readEntity(String.class);
        }else{
            return null;
        }
    }

    public String downloadKafka(String hdfsConfigXMLPath, Project project ,DatasetStructure datasetStructure,Users user, String publicDsId, List<String> partners, String sessionId, String topicName, String keyStorePath, String trustStorePath,String brokerEndpoint,String restEndpoint,String domain) throws IOException {
        
        String dsPath = datasetController.createDatasetForDownload(user, project, datasetStructure, publicDsId);
        
        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(new HdfsResource(hdfsConfigXMLPath, dsPath,datasetStructure.getChildrenFiles().get(0),hdfsUsersController.getHdfsUserName(project, user)),new KafkaResource(brokerEndpoint,restEndpoint,domain,sessionId,String.valueOf(project.getId()),topicName,keyStorePath,trustStorePath), new HopsResource(project.getId()),new TorrentId(publicDsId), partners);
        
        rest_client = ClientBuilder.newClient();
        
        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/download/xml");
        
        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadGVoDJson, MediaType.APPLICATION_JSON), Response.class);
        
        if(r != null && r.getStatus() == 200){
            return r.readEntity(String.class);
        }else{
            return null;
        }
    }
    
}
