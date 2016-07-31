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
import io.hops.gvod.io.download.DownloadGVoDJson;
import io.hops.gvod.io.requestmanifest.DownloadRequest;
import io.hops.gvod.io.resources.HDFSEndpoint;
import io.hops.gvod.io.resources.HDFSResource;
import io.hops.gvod.io.resources.HopsResource;
import io.hops.gvod.io.resources.KafkaResource;
import io.hops.gvod.io.resources.items.AddressJSON;
import io.hops.gvod.io.resources.items.ManifestJson;
import io.hops.gvod.io.resources.items.TorrentId;
import io.hops.gvod.io.stop.StopGVoDJson;
import io.hops.gvod.io.upload.UploadGVoDJson;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFileSystemOps;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFsService;
import se.kth.hopsworks.hdfsUsers.controller.HdfsUsersController;
import se.kth.hopsworks.rest.AppException;
import se.kth.hopsworks.user.model.Users;
import se.kth.hopsworks.util.Settings;
import se.kth.hopsworks.dataset.Dataset;
import java.util.Map;
import se.kth.bbc.project.fb.Inode;
import se.kth.bbc.project.fb.InodeFacade;
import se.kth.hopsworks.dataset.DatasetFacade;
import io.hops.gvod.io.status.StatusGVoDJson;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class GVoDController {

    @EJB
    Settings settings;

    @EJB
    DatasetController datasetController;

    @EJB
    private HdfsUsersController hdfsUsersController;

    @EJB
    private DistributedFsService dfs;

    @EJB
    private HdfsUsersController hdfsUsersBean;

    @EJB
    private DatasetFacade datasetFacade;

    @EJB
    private InodeFacade inodeFacade;

    private WebTarget webTarget = null;
    private Client rest_client = null;

    public String uploadToGVod(Project project, Dataset dataset, String username, String datasetPath) {

        UploadGVoDJson uploadGVoDJson = new UploadGVoDJson(
                new TorrentId(settings.getCLUSTER_ID() + "_" + project.getName() + "_" + dataset.getName()),
                new HDFSResource(datasetPath),
                new HDFSEndpoint(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME, username)
                );

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/upload/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(uploadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public ManifestJson downloadRequest(String publicDsId, String hdfsConfigXMLPath, Users user, Project project, String datasetName, List<AddressJSON> partners) throws AppException {

        DistributedFileSystemOps dfso = null;
        DistributedFileSystemOps udfso = null;
        Dataset ds = null;
        String username = hdfsUsersBean.getHdfsUserName(project, user);
        try {
            dfso = dfs.getDfsOps();
            if (username != null) {
                udfso = dfs.getDfsOps(username);
            }
            datasetController.createDataset(user, project, datasetName, "", -1, true, false, dfso, udfso);

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
                new TorrentId(publicDsId),
                new HDFSResource(File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator),
                partners
        );

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("/torrent/hops/download/start/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(downloadRequest, MediaType.APPLICATION_JSON), Response.class);

        String pathToManifest = null;
        
        if (r != null && r.getStatus() == 200) {
            pathToManifest =  r.readEntity(String.class);
        } else {
            return null;
        }
        
        ManifestJson manifestJson = null;
        
        if (pathToManifest != null) {
            byte [] jsonBytes = datasetController.readJsonFromHdfs(pathToManifest);
            manifestJson = datasetController.getManifestJSON(jsonBytes);

        } else {
            try {
                dfso = dfs.getDfsOps();
                datasetController.deleteDataset(
                        File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator,
                        user,
                        project,
                        dfso);
            } catch (Exception e) {
                throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), e.
                        getLocalizedMessage());
            } finally {
                if (dfso != null) {
                    dfso.close();
                }
            }
        }
        return manifestJson;
    }

    public String downloadHdfs(String hdfsConfigXMLPath, Project project, Users user, String publicDsId, List<String> partners, String datasetName) throws AppException {

        String dsPath = File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator;

        Inode inode = inodeFacade.getInodeAtPath(dsPath);

        Dataset ds = datasetFacade.findByProjectAndInode(project, inode);

        ds.setPublicDs(true);
        ds.setPublicDsId(publicDsId);
        ds.setEditable(false);

        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(
                new HDFSResource(hdfsConfigXMLPath, hdfsUsersController.getHdfsUserName(project, user)),
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

    public String downloadKafka(String hdfsConfigXMLPath, Project project, Users user, String publicDsId, List<String> partners, String sessionId, Map topics, String keyStorePath, String trustStorePath, String brokerEndpoint, String restEndpoint, String domain, String datasetName) throws AppException {

        String dsPath = File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + datasetName + File.separator;

        Inode inode = inodeFacade.getInodeAtPath(dsPath);

        Dataset ds = datasetFacade.findByProjectAndInode(project, inode);

        ds.setPublicDs(true);
        ds.setPublicDsId(publicDsId);
        ds.setEditable(false);

        DownloadGVoDJson downloadGVoDJson = new DownloadGVoDJson(
                new HDFSResource(hdfsConfigXMLPath, hdfsUsersController.getHdfsUserName(project, user)),
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

    public String getStatusOfDataset(String dsPath, String publicDsId) {

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
