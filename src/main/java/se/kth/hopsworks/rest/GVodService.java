/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.rest;

import com.owlike.genson.Genson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.json.JSONObject;
import se.kth.bbc.project.Project;
import se.kth.bbc.project.ProjectFacade;
import se.kth.hopsworks.certificates.UserCerts;
import se.kth.hopsworks.certificates.UserCertsFacade;
import se.kth.hopsworks.dataset.DatasetStructure;

import se.kth.hopsworks.filters.AllowedRoles;
import se.kth.hopsworks.gvod.downloadHdfs.DownloadHdfsJson;
import se.kth.hopsworks.gvod.GVodController;
import se.kth.hopsworks.gvod.downloadHdfsKafka.DownloadHdfsKafkaJson;
import se.kth.hopsworks.hdfsUsers.controller.HdfsUsersController;
import se.kth.hopsworks.user.model.Users;
import se.kth.hopsworks.users.UserFacade;
import se.kth.hopsworks.util.Settings;

/**
 *
 * @author jsvhqr
 */
@Path("/gvod")
@RolesAllowed({"HOPS_ADMIN", "HOPS_USER"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@TransactionAttribute(TransactionAttributeType.NEVER)
public class GVodService {

    private WebTarget webTarget;
    private Client rest_client;
    private Genson genson = new Genson();

    @EJB
    private UserFacade userFacade;

    @EJB
    private Settings settings;
    @EJB
    private NoCacheResponse noCacheResponse;
    @EJB
    private GVodController gvodController;
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private UserCertsFacade userCerts;
    @EJB
    private HdfsUsersController hdfsUsersBean;

    @PUT
    @Path("downloadhdfs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles(roles = {AllowedRoles.DATA_SCIENTIST, AllowedRoles.DATA_OWNER})
    public Response downloadDatasetHdfs(@Context SecurityContext sc,
            @Context HttpServletRequest req, DownloadHdfsJson downloadHdfsJson) throws IOException {
        
                String response = gvodController.downloadHdfs(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME,
                projectFacade.find(downloadHdfsJson.getProjectId()),
                downloadHdfsJson.getDatasetStructure(),
                userFacade.findByEmail(sc.getUserPrincipal().getName()),
                downloadHdfsJson.getDatasetId(),
                downloadHdfsJson.getPartners());

        if (response != null) {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(response).build();
        } else {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.EXPECTATION_FAILED).entity(null).build();
        }
    }

    @PUT
    @Path("downloadkafka")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles(roles = {AllowedRoles.DATA_SCIENTIST, AllowedRoles.DATA_OWNER})
    public Response downloadDatasetKafka(@Context SecurityContext sc,
            @Context HttpServletRequest req, DownloadHdfsKafkaJson downloadHdfsKafkaJson) throws IOException {
        
        Project project = projectFacade.find(downloadHdfsKafkaJson.getProjectId());
        String certPath = getCertificatePaths(project);

        String response = gvodController.downloadKafka(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME,
                project,
                downloadHdfsKafkaJson.getDatasetStructure(),
                userFacade.findByEmail(sc.getUserPrincipal().getName()),
                downloadHdfsKafkaJson.getDatasetId(),
                downloadHdfsKafkaJson.getPartners(),
                req.getSession().getId(),
                downloadHdfsKafkaJson.getTopicName(),
                certPath + "/keystore.jks",
                certPath + "/truststore.jks",
                settings.getKafkaConnectStr(),
                "http://" + settings.getDOMAIN() + ":" + settings.getRestPort(),
                settings.getDOMAIN());

        if (response != null) {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(response).build();
        } else {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.EXPECTATION_FAILED).entity(null).build();
        }
    }

    private String getCertificatePaths(Project project) {
        UserCerts userCert = userCerts.findUserCert(project.getName(), project.getOwner().getUsername());
        //Check if the user certificate was actually retrieved
        if (userCert.getUserCert() != null
                && userCert.getUserCert().length > 0
                && userCert.getUserKey() != null
                && userCert.getUserKey().length > 0) {
            
            File certDir = new File(Settings.KAFKA_TMP_CERT_STORE_LOCAL);
            
            if (!certDir.exists()) {
                      try{
                          certDir.mkdir();
                      } 
                      catch(Exception ex){
                          
                      }        
            }
            try{
                FileOutputStream fos = new FileOutputStream(Settings.KAFKA_TMP_CERT_STORE_LOCAL + "/keystore.jks");
                fos.write(userCert.getUserCert());
                fos.close();
                
                fos = new FileOutputStream(Settings.KAFKA_TMP_CERT_STORE_LOCAL + "/truststore.jks");
                fos.write(userCert.getUserKey());
                fos.close();
                
            }catch(Exception e){
                
            }      
            
        }else{
            return null;
        }

        return Settings.KAFKA_TMP_CERT_STORE_LOCAL;
    }
}
