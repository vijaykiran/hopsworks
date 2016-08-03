/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.controller;

import io.hops.gvod.io.contents.HopsContentsReqJSON;
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
import io.hops.gvod.io.startdownload.StartDownloadJSON;
import io.hops.gvod.io.resources.HDFSEndpoint;
import io.hops.gvod.io.resources.HDFSResource;
import io.hops.gvod.io.resources.KafkaEndpoint;
import io.hops.gvod.io.resources.items.AddressJSON;
import io.hops.gvod.io.resources.items.ExtendedDetails;
import io.hops.gvod.io.resources.items.KafkaResource;
import io.hops.gvod.io.resources.items.ManifestJson;
import io.hops.gvod.io.resources.items.TorrentId;
import io.hops.gvod.io.responses.ErrorDescJSON;
import io.hops.gvod.io.responses.HopsContentsSummaryJSON;
import io.hops.gvod.io.responses.SuccessJSON;
import io.hops.gvod.io.stop.RemoveGVodJSON;
import io.hops.gvod.io.upload.UploadGVoDJSON;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFileSystemOps;
import se.kth.hopsworks.hdfs.fileoperations.DistributedFsService;
import se.kth.hopsworks.hdfsUsers.controller.HdfsUsersController;
import se.kth.hopsworks.rest.AppException;
import se.kth.hopsworks.user.model.Users;
import se.kth.hopsworks.util.Settings;
import java.util.Map;
import io.hops.gvod.io.status.StatusGVoDJSON;
import java.util.HashMap;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import se.kth.hopsworks.dataset.Dataset;

/**
 *
 * @author jsvhqr
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
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

    public String uploadToGVod(String projectName, String sourceDatasetName, String username, String datasetPath) {

        UploadGVoDJSON uploadGVoDJSON = new UploadGVoDJSON(
                new TorrentId(Settings.getPublicDatasetId(settings.getCLUSTER_ID(), projectName, sourceDatasetName)),
                sourceDatasetName,
                new HDFSResource(datasetPath, Settings.MANIFEST_NAME),
                new HDFSEndpoint(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME, username)
        );
        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/upload/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(uploadGVoDJSON, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(SuccessJSON.class).getDetails();
        } else if (r != null) {
            return r.readEntity(ErrorDescJSON.class).getDetails();
        } else {
            return null;
        }
    }

    public ManifestJson startDownload(String publicDsId, Users user, Project project, String destinationDatasetName, List<AddressJSON> partners) throws AppException {

        DistributedFileSystemOps dfso = null;
        DistributedFileSystemOps udfso = null;
        String username = hdfsUsersBean.getHdfsUserName(project, user);
        try {
            dfso = dfs.getDfsOps();
            if (username != null) {
                udfso = dfs.getDfsOps(username);
            }
            Dataset ds =  datasetController.createDataset(user, project, destinationDatasetName, "", -1, true, false, dfso, udfso);
            datasetController.makeDatasetPublicAndImmutable(ds, publicDsId);
            
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

        StartDownloadJSON startDownloadJSON = new StartDownloadJSON(
                new TorrentId(publicDsId),
                destinationDatasetName,
                new HDFSResource(Settings.getProjectPath(project.getName()) + File.separator + destinationDatasetName + File.separator, "manifest.json"),
                partners,
                new HDFSEndpoint(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME, username)
        );

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("/torrent/hops/download/start/xml");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(startDownloadJSON, MediaType.APPLICATION_JSON), Response.class);

        String pathToManifest = Settings.getProjectPath(project.getName()) + File.separator + destinationDatasetName + File.separator + Settings.MANIFEST_NAME;

        ManifestJson manifestJson = null;

        if (r != null && r.getStatus() == 200) {
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

                if (!entry.getValue().equals("")) {
                    kafkaResources.put(entry.getKey(), new KafkaResource(sessiodId, entry.getValue()));
                } else {
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
                return r.readEntity(SuccessJSON.class).getDetails();
            } else if (r != null) {
                return r.readEntity(ErrorDescJSON.class).getDetails();
            } else {
                return null;
            }

        }

    }

    public String removeUpload(String dsPath, String publicDsId) {

        RemoveGVodJSON removeGVoDJSON = new RemoveGVodJSON(dsPath, new TorrentId(publicDsId));

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/stop");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(removeGVoDJSON, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(SuccessJSON.class).getDetails();
        } else if(r != null){
            return r.readEntity(ErrorDescJSON.class).getDetails();
        }else{
            return null;
        }

    }
    
    public HopsContentsSummaryJSON getContents(int projectId){
        
        HopsContentsReqJSON hopsContentsReqJSON = new HopsContentsReqJSON(projectId);
        
        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("t/library/contents");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(hopsContentsReqJSON, MediaType.APPLICATION_JSON), Response.class);
        
        if (r != null && r.getStatus() == 200) {
            return r.readEntity(HopsContentsSummaryJSON.class);
        } else{
            return null;
        }
    }

    public String getStatusOfDataset(String dsPath, String publicDsId) {

        StatusGVoDJSON statusGVoDJSON = new StatusGVoDJSON(dsPath, new TorrentId(publicDsId));

        rest_client = ClientBuilder.newClient();

        webTarget = rest_client.target(settings.getGVOD_REST_ENDPOINT()).path("torrent/hops/stop");

        Response r = webTarget.request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(statusGVoDJSON, MediaType.APPLICATION_JSON), Response.class);

        if (r != null && r.getStatus() == 200) {
            return r.readEntity(String.class);
        } else {
            return null;
        }
    }
}
