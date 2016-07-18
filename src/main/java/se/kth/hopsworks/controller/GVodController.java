/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.controller;

import java.io.File;
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
import se.kth.bbc.project.Project;
import se.kth.bbc.project.ProjectFacade;
import se.kth.hopsworks.dataset.DatasetStructureJson;
import se.kth.hopsworks.gvod.download.DownloadGVoDJson;
import se.kth.hopsworks.gvod.resources.HdfsResource;
import se.kth.hopsworks.gvod.resources.HopsResource;
import se.kth.hopsworks.gvod.resources.KafkaResource;
import se.kth.hopsworks.gvod.stop.StopGVoDJson;
import se.kth.hopsworks.gvod.upload.UploadGVoDJson;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFileSystemOps;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFsService;
import se.kth.hopsworks.hdfsUsers.controller.HdfsUsersController;
import se.kth.hopsworks.rest.AppException;
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
    
    @EJB
    private DistributedFsService dfs;
    
    @EJB
    private HdfsUsersController hdfsUsersBean;

    private WebTarget webTarget = null;
    private Client rest_client = null;

    public String uploadToGVod(int projectId, String hdfsConfigXMLPath, String path, DatasetStructureJson datasetStructure, String username, String publicDsId) {

        UploadGVoDJson uploadGVoDJson = new UploadGVoDJson(new HdfsResource(hdfsConfigXMLPath, path, null, username), new HopsResource(projectId), new TorrentId(publicDsId));

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/upload/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(uploadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public String downloadHdfs(String hdfsConfigXMLPath, Project project, DatasetStructureJson datasetStructure, Users user, String publicDsId, List<String> partners) throws IOException, AppException {

        DistributedFileSystemOps dfso = null;
        DistributedFileSystemOps udfso = null;
        try {
            dfso = dfs.getDfsOps();
            String username = hdfsUsersBean.getHdfsUserName(project, user);
            if (username != null) {
                udfso = dfs.getDfsOps(username);
            }
            datasetController.createDataset(user, project, datasetStructure.getName(), datasetStructure.getDescription(), -1, true, false, dfso, udfso, true, publicDsId);
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
        
        String dsPath = File.separator + Settings.DIR_ROOT + File.separator
                + project.getName();
        dsPath = dsPath + File.separator + datasetStructure.getName();
        
        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(new HdfsResource(hdfsConfigXMLPath, 
                dsPath, datasetStructure.getChildrenFiles().get(0), hdfsUsersController.getHdfsUserName(project, user)), null, new HopsResource(project.getId()), new TorrentId(publicDsId), partners);

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/download/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public String downloadKafka(String hdfsConfigXMLPath, Project project, DatasetStructureJson datasetStructure, Users user, String publicDsId, List<String> partners, String sessionId, String topicName, String keyStorePath, String trustStorePath, String brokerEndpoint, String restEndpoint, String domain) throws IOException, AppException {

        DistributedFileSystemOps dfso = null;
        DistributedFileSystemOps udfso = null;
        try {
            dfso = dfs.getDfsOps();
            String username = hdfsUsersBean.getHdfsUserName(project, user);
            if (username != null) {
                udfso = dfs.getDfsOps(username);
            }
            datasetController.createDataset(user, project, datasetStructure.getName(), datasetStructure.getDescription(), -1, true, false, dfso, udfso, true, publicDsId);
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
        
        String dsPath = File.separator + Settings.DIR_ROOT + File.separator
                + project.getName();
        dsPath = dsPath + File.separator + datasetStructure.getName();

        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(new HdfsResource(hdfsConfigXMLPath, dsPath, datasetStructure.getChildrenFiles().get(0), hdfsUsersController.getHdfsUserName(project, user)), new KafkaResource(brokerEndpoint, restEndpoint, domain, sessionId, String.valueOf(project.getId()), topicName, keyStorePath, trustStorePath), new HopsResource(project.getId()), new TorrentId(publicDsId), partners);

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/download/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public String removeUpload(String name, String publicDsId) {
        
        StopGVoDJson stopGvodJson = new StopGVoDJson(name, new TorrentId(publicDsId));
        
        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/stop");
        
        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(stopGvodJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
        
    }

}
