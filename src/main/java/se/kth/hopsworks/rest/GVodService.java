/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.rest;

import java.io.File;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import se.kth.bbc.project.Project;
import se.kth.bbc.security.ua.UserManager;
import se.kth.hopsworks.controller.KafkaController;
import se.kth.hopsworks.controller.ResponseMessages;
import se.kth.hopsworks.filters.AllowedRoles;
import io.hops.gvod.io.download.HDFSDownloadDTO;
import se.kth.hopsworks.controller.GVoDController;
import se.kth.hopsworks.controller.ProjectController;
import io.hops.gvod.io.download.HDFSKafkaDownloadDTO;
import io.hops.gvod.io.download.startDownloadDTO;
import io.hops.gvod.io.resources.KafkaEndpoint;
import io.hops.gvod.io.resources.items.ManifestJson;
import se.kth.hopsworks.hdfsUsers.controller.HdfsUsersController;
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


    @EJB
    private Settings settings;
    @EJB
    private NoCacheResponse noCacheResponse;
    @EJB
    private GVoDController gvodController;
    @EJB
    private ProjectController projectController;
    @EJB
    private KafkaController kafkaController;
    @EJB
    private UserManager userBean;
    @EJB
    private HdfsUsersController hdfsUsersBean;
    
    @PUT
    @Path("startdownload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles(roles = {AllowedRoles.DATA_SCIENTIST, AllowedRoles.DATA_OWNER})
    public Response startDownload(@Context SecurityContext sc,
            @Context HttpServletRequest req, startDownloadDTO startDownloadDTO) throws AppException {

        if (settings.getCLUSTER_ID() == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    ResponseMessages.NOT_REGISTERD_WITH_HOPS_SITE);
        }
        if (settings.getGVOD_UDP_ENDPOINT() == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    ResponseMessages.GVOD_OFFLINE);
        }

        
        ManifestJson response = gvodController.startDownload(startDownloadDTO.getPublicDatasetId(), 
                userBean.getUserByEmail(sc.getUserPrincipal().getName()), 
                projectController.findProjectById(startDownloadDTO.getProjectId()), 
                startDownloadDTO.getDestinationDatasetName(),
                startDownloadDTO.getPartners());

        if (response != null) {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(response).build();
        } else {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.EXPECTATION_FAILED).entity(null).build();
        }
    }
    
    @PUT
    @Path("downloadhdfs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles(roles = {AllowedRoles.DATA_SCIENTIST, AllowedRoles.DATA_OWNER})
    public Response downloadDatasetHdfs(@Context SecurityContext sc,
            @Context HttpServletRequest req, HDFSDownloadDTO hdfsDownloadDTO) throws AppException {

        if (settings.getCLUSTER_ID() == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    ResponseMessages.NOT_REGISTERD_WITH_HOPS_SITE);
        }
        if (settings.getGVOD_UDP_ENDPOINT() == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    ResponseMessages.GVOD_OFFLINE);
        }
        
        Project project = projectController.findProjectById(hdfsDownloadDTO.getProjectId());
        String response = gvodController.download(
                null, 
                hdfsUsersBean.getHdfsUserName(project, userBean.getUserByEmail(sc.getUserPrincipal().getName())), 
                hdfsDownloadDTO.getPublicDatasetId(), 
                Settings.getProjectPath(project.getName()) + File.separator + hdfsDownloadDTO.getDestinationDatasetName() + File.separator, 
                hdfsDownloadDTO.getFileTopics(), 
                null);

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
            @Context HttpServletRequest req, HDFSKafkaDownloadDTO hdfsKafkaDownloadDTO) throws AppException {
        
        if (settings.getCLUSTER_ID() == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    ResponseMessages.NOT_REGISTERD_WITH_HOPS_SITE);
        }
        if(settings.getGVOD_UDP_ENDPOINT() == null){
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    ResponseMessages.GVOD_OFFLINE);
        }

        Project project = projectController.findProjectById(hdfsKafkaDownloadDTO.getProjectId());
        String certPath = kafkaController.getKafkaCertPaths(project);
        
        String response = gvodController.download(new KafkaEndpoint(settings.getKafkaConnectStr(), "http://" + settings.getDOMAIN() + ":" + settings.getRestPort(), settings.getDOMAIN(), String.valueOf(hdfsKafkaDownloadDTO.getProjectId()), certPath + "/keystore.jks", certPath + "/truststore.jks"), 
                hdfsUsersBean.getHdfsUserName(project, userBean.getUserByEmail(sc.getUserPrincipal().getName())), 
                hdfsKafkaDownloadDTO.getPublicDatasetId(), 
                Settings.getProjectPath(project.getName()) + File.separator + hdfsKafkaDownloadDTO.getDestinationDatasetName() + File.separator, 
                hdfsKafkaDownloadDTO.getTopics(), 
                req.getSession().getId());

        if (response != null) {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(response).build();
        } else {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.EXPECTATION_FAILED).entity(null).build();
        }
    }
}
