/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.rest;

import com.google.common.io.Files;
import com.owlike.genson.Genson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
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
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.json.JSONObject;
import se.kth.bbc.project.Project;
import se.kth.bbc.project.ProjectFacade;
import se.kth.hopsworks.certificates.UserCerts;
import se.kth.hopsworks.certificates.UserCertsFacade;
import se.kth.hopsworks.controller.LocalResourceDTO;
import se.kth.hopsworks.dataset.DatasetStructure;

import se.kth.hopsworks.filters.AllowedRoles;
import se.kth.hopsworks.gvod.GVodController;
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
            @Context HttpServletRequest req, String json) throws IOException {

        JSONObject jsonObject = new JSONObject(json);
        String email = sc.getUserPrincipal().getName();
        int projectId = jsonObject.getInt("projectId");
        Project project = projectFacade.find(projectId);
        Users user = userFacade.findByEmail(email);
        String response = gvodController.downloadHdfs(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME,
                project,
                new DatasetStructure(jsonObject.getString("datasetStructure")),
                user,
                jsonObject.getString("datasetId"),
                jsonObject.getJSONArray("partners"));

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
            @Context HttpServletRequest req, String json) throws IOException {

        JSONObject jsonObject = new JSONObject(json);
        int projectId = jsonObject.getInt("projectId");
        Project project = projectFacade.find(projectId);
        String email = sc.getUserPrincipal().getName();
        Users user = userFacade.findByEmail(email);
        String certPath = getCertificatePaths(user, project);
        String keyStorePath = certPath + "keystore.jks";
        String trustStorePath = certPath + "truststore.jks";
        String brokerEndpoint = settings.getKafkaConnectStr();
        String restEndpoint = settings.getDOMAIN() + settings.getRestPort() + "api/project/";
        String domain = settings.getDOMAIN();

        String response = gvodController.downloadKafka(settings.getHadoopConfDir() + File.separator + Settings.DEFAULT_HADOOP_CONFFILE_NAME,
                project,
                new DatasetStructure(jsonObject.getString("datasetStructure")),
                user,
                jsonObject.getString("datasetId"),
                jsonObject.getJSONArray("partners"),
                req.getSession().getId(),
                jsonObject.getString("topicName"),
                keyStorePath,
                trustStorePath,
                brokerEndpoint,
                restEndpoint,
                domain);

        if (response != null) {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(response).build();
        } else {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.EXPECTATION_FAILED).entity(null).build();
        }
    }

    private String getCertificatePaths(Users user, Project project) {
        UserCerts userCert = userCerts.findUserCert(project.getName(), hdfsUsersBean.getHdfsUserName(project, user));
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
