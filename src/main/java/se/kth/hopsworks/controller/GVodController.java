/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
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
import org.json.JSONObject;
import se.kth.bbc.project.Project;
import se.kth.bbc.project.ProjectFacade;
import se.kth.hopsworks.gvod.io.download.DownloadGVoDJson;
import se.kth.hopsworks.gvod.io.requestmanifest.DownloadRequest;
import se.kth.hopsworks.gvod.io.resources.HdfsResource;
import se.kth.hopsworks.gvod.io.resources.HopsResource;
import se.kth.hopsworks.gvod.io.resources.KafkaResource;
import se.kth.hopsworks.gvod.io.resources.items.ManifestJson;
import se.kth.hopsworks.gvod.io.resources.items.TorrentId;
import se.kth.hopsworks.gvod.io.stop.StopGVoDJson;
import se.kth.hopsworks.gvod.io.upload.UploadGVoDJson;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFileSystemOps;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFsService;
import se.kth.hopsworks.hdfsUsers.controller.HdfsUsersController;
import se.kth.hopsworks.rest.AppException;
import se.kth.hopsworks.user.model.Users;
import se.kth.hopsworks.users.UserFacade;
import se.kth.hopsworks.util.Settings;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import se.kth.hopsworks.dataset.Dataset;
import java.util.Map;
import se.kth.hopsworks.gvod.io.status.StatusGVoDJson;

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

    @EJB
    private DistributedFsService dfs;

    @EJB
    private HdfsUsersController hdfsUsersBean;
    

    private WebTarget webTarget = null;
    private Client rest_client = null;

    public String uploadToGVod(Project project, Dataset dataset, String username, String datasetPath) throws JsonProcessingException, AppException {

        UploadGVoDJson uploadGVoDJson = new UploadGVoDJson(
                new HdfsResource(File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + dataset.getName() + File.separator, username), 
                new HopsResource(project.getId()), 
                new TorrentId(settings.getCLUSTER_ID() + "_" + project.getName() + "_" + dataset.getName()),
                datasetPath);

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/upload/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(uploadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public ManifestJson downloadRequest(String publicDsId, String hdfsConfigXMLPath, Users user, Project project, String datasetName) throws IOException, ParseException, AppException {
        
        DistributedFileSystemOps dfso = null;
        DistributedFileSystemOps udfso = null;
        try {
            dfso = dfs.getDfsOps();
            String username = hdfsUsersBean.getHdfsUserName(project, user);
            if (username != null) {
                udfso = dfs.getDfsOps(username);
            }
            Dataset ds = datasetController.createDataset(user, project, datasetName, "", -1, true, false, dfso, udfso);
            ds.setPublicDs(true);
            ds.setPublicDsId(publicDsId);
            ds.setEditable(false);
            
        } catch (NullPointerException c) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), c.
                    getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    "Failed to create dataset: " + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.
                    getStatusCode(), "Failed to create dataset: " + e.
                    getLocalizedMessage());
        } finally {
            if (dfso != null) {
                dfso.close();
            }
            if (udfso != null) {
                udfso.close();
            }
        }
        
        DownloadRequest downloadRequest = new DownloadRequest(
                new HdfsResource(hdfsConfigXMLPath, hdfsUsersController.getHdfsUserName(project, user)),
                publicDsId,
                Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator);

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/manifest/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadRequest, MediaType.APPLICATION_JSON), Response.class);

        String pathToManifest = null;
        
        if (r != null && r.getStatus() == 200) {
            pathToManifest =  r.readEntity(String.class);
        } else {
            return null;
        }
        
        if(pathToManifest != null){
            
            dfso = dfs.getDfsOps();
            JSONParser parser = new JSONParser();
            dfso.copyFromHDFSToLocal(pathToManifest, "/tmp/in/" + project.getName() + "/manifest.json");
            Object obj = parser.parse(new FileReader("/tmp/in/" + project.getName() + "/manifest.json"));
            JSONObject jsonObject = (JSONObject)obj;
            String jsonString = new Gson().toJson(jsonObject);
            ManifestJson manifestJson = new Gson().fromJson(jsonString, ManifestJson.class);
            
            return manifestJson;
            
        }else{
            datasetController.deleteDataset(Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator, user, project, dfso);
            return null;
        }
        
    }

    public String downloadHdfs(String hdfsConfigXMLPath, Project project, Users user, String publicDsId, List<String> partners, String datasetName) throws IOException, AppException {

        String dsPath = File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator;

        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(
                new HdfsResource(hdfsConfigXMLPath,
                hdfsUsersController.getHdfsUserName(project, user)), 
                null, 
                new HopsResource(project.getId()), 
                new TorrentId(publicDsId), 
                partners,
                dsPath);

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/download/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public String downloadKafka(String hdfsConfigXMLPath, Project project, Users user, String publicDsId, List<String> partners, String sessionId, Map topics, String keyStorePath, String trustStorePath, String brokerEndpoint, String restEndpoint, String domain, String datasetName) throws IOException, AppException {

        String dsPath = File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator;

        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(
                new HdfsResource(hdfsConfigXMLPath,hdfsUsersController.getHdfsUserName(project, user)), 
                new KafkaResource(brokerEndpoint, restEndpoint, domain, sessionId, String.valueOf(project.getId()), topics, keyStorePath, trustStorePath), 
                new HopsResource(project.getId()), 
                new TorrentId(publicDsId), 
                partners,
                dsPath);

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/download/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public String removeUpload(String dsPath, String publicDsId) {

        StopGVoDJson stopGvodJson = new StopGVoDJson(dsPath, new TorrentId(publicDsId));

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/stop");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(stopGvodJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }

    }
    
    public String getStatusOfDataset(String dsPath, String publicDsId){
        
        StatusGVoDJson statusGVoDJson = new StatusGVoDJson(dsPath, new TorrentId(publicDsId));

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/stop");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(statusGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

}
