/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.controller;

import io.hops.gvod.io.download.DownloadGVoDJSON;
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
import io.hops.gvod.io.requestmanifest.StartDownloadJSON;
import io.hops.gvod.io.resources.HDFSEndpoint;
import io.hops.gvod.io.resources.HDFSResource;
import io.hops.gvod.io.resources.KafkaEndpoint;
import io.hops.gvod.io.resources.items.AddressJSON;
import io.hops.gvod.io.resources.items.ExtendedDetails;
import io.hops.gvod.io.resources.items.KafkaResource;
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
import io.hops.gvod.io.status.StatusGVoDJson;
import java.util.HashMap;

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
    private DistributedFsService dfs;

    @EJB
    private HdfsUsersController hdfsUsersBean;

    private WebTarget webTarget = null;
    private Client rest_client = null;

    public String uploadToGVod(String projectName, String datasetName, String username, String datasetPath) {

        UploadGVoDJson uploadGVoDJson = new UploadGVoDJson(
                new TorrentId(Settings.getPublicDatasetId(settings.getCLUSTER_ID(), projectName, datasetName)),
                new HDFSResource(datasetPath, Settings.MANIFEST_NAME),
                new HDFSEndpoint(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME, username)
        );
        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/upload/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(uploadGVoDJson, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }

    public ManifestJson startDownload(String publicDsId, Users user, Project project, String destinationDatasetName, List<AddressJSON> partners) throws AppException {

        DistributedFileSystemOps dfso = null;
        DistributedFileSystemOps udfso = null;
        Dataset ds = null;
        String username = hdfsUsersBean.getHdfsUserName(project, user);
        try {
            dfso = dfs.getDfsOps();
            if (username != null) {
                udfso = dfs.getDfsOps(username);
            }
            datasetController.createDataset(user, project, destinationDatasetName, "", -1, true, false, dfso, udfso);

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

        StartDownloadJSON downloadRequest = new StartDownloadJSON(
                new TorrentId(publicDsId),
                new HDFSResource(File.separator + Settings.DIR_ROOT + File.separator + project.getName() + File.separator + destinationDatasetName + File.separator, "manifest.json"),
                partners,
                new HDFSEndpoint(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME, username)
        );

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("/torrent/hops/download/start/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(downloadRequest, MediaType.APPLICATION_JSON), Response.class);

        String pathToManifest = null;

        if (r != null && r.getStatus() == 200) {
            pathToManifest = r.readEntity(String.class);
        } else {
            return null;
        }

        ManifestJson manifestJson = null;

        if (pathToManifest != null) {
            byte[] jsonBytes = datasetController.readJsonFromHdfs(pathToManifest);
            manifestJson = datasetController.getManifestJSON(jsonBytes);

        } else {
            try {
                dfso = dfs.getDfsOps();
                datasetController.deleteDataset(Settings.getProjectPath(project.getName()) + File.separator + destinationDatasetName + File.separator,
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

    public String download(KafkaEndpoint kafkaEndpoint, String username, String publicDsId, String dsPath, Map<String, String> fileTopics, String sessiodId) {
        
        if (kafkaEndpoint != null) { // perform kafka download

            Map<String, HDFSResource> hdfsResources = new HashMap<>();
            Map<String, KafkaResource> kafkaResources = new HashMap<>();
            for (Map.Entry<String, String> entry : fileTopics.entrySet()) {

                if(!entry.getValue().equals("")){
                    kafkaResources.put(entry.getKey(), new KafkaResource(sessiodId, entry.getValue()));
                }else{
                    hdfsResources.put(entry.getKey(), new HDFSResource(dsPath, entry.getKey()));
                }

            }
            DownloadGVoDJSON downloadGVodJSON = new DownloadGVoDJSON(
                    new TorrentId(publicDsId),
                    kafkaEndpoint,
                    new HDFSEndpoint(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME, username),
                    new ExtendedDetails(hdfsResources, kafkaResources)
            );

            rest_client = ClientBuilder.newClient();

            webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("/torrent/hops/download/advance/xml");

            Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(downloadGVodJSON, MediaType.APPLICATION_JSON), Response.class);

            if (r != null && r.getStatus() == 200) {
                return r.readEntity(String.class);
            } else {
                return null;
            }

        } else { // only hdfs

            Map<String, HDFSResource> hdfsResources = new HashMap<>();
            for (Map.Entry<String, String> entry : fileTopics.entrySet()) {

                hdfsResources.put(entry.getKey(), new HDFSResource(dsPath, entry.getKey()));

            }
            DownloadGVoDJSON downloadGVodJSON = new DownloadGVoDJSON(
                    new TorrentId(publicDsId),
                    null,
                    new HDFSEndpoint(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME, username),
                    new ExtendedDetails(hdfsResources, null)
            );

            rest_client = ClientBuilder.newClient();

            webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("/torrent/hops/download/advance/xml");

            Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(downloadGVodJSON, MediaType.APPLICATION_JSON), Response.class);

            if (r != null && r.getStatus() == 200) {
                return r.readEntity(String.class);
            } else {
                return null;
            }

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
