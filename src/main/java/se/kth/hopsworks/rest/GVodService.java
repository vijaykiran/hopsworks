/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.rest;

import com.owlike.genson.Genson;
import java.io.File;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.json.JSONArray;
import org.json.JSONObject;
import se.kth.bbc.project.ProjectFacade;
import se.kth.hopsworks.controller.UsersController;

import se.kth.hopsworks.filters.AllowedRoles;
import se.kth.hopsworks.gvod.GVodController;
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
    private ProjectFacade projectFacade;
    private Genson genson = new Genson();

    private UsersController usersController;

    @EJB
    private Settings settings;
    @EJB
    private NoCacheResponse noCacheResponse;
    @EJB
    private GVodController gvodController;

    @GET
    @Path("downloadhdfs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles(roles = {AllowedRoles.DATA_SCIENTIST, AllowedRoles.DATA_OWNER})
    public Response downloadDatasetHdfs(@Context SecurityContext sc,
            @Context HttpServletRequest req, String json) {

        JSONObject jsonObject = new JSONObject(json);

        String response = gvodController.downloadHdfs(settings.getGVOD_REST_ENDPOINT(),
                File.separator + Settings.DIR_ROOT + File.separator + projectFacade.find(jsonObject.getString("projectId")).getName() + File.separator,
                jsonObject.getString("datasetName"),
                usersController.generateUsername(sc.getUserPrincipal().getName()),
                jsonObject.getString("datasetId"),
                jsonObject.getJSONArray("partners"));

        if (response != null) {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(response).build();
        } else {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.EXPECTATION_FAILED).entity(null).build();
        }
    }

    @GET
    @Path("downloadkafka")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles(roles = {AllowedRoles.DATA_SCIENTIST, AllowedRoles.DATA_OWNER})
    public Response downloadDatasetKafka(@Context SecurityContext sc,
            @Context HttpServletRequest req, String json) {

        JSONObject jsonObject = new JSONObject(json);

        String response = gvodController.downloadKafka();

        if (response != null) {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(response).build();
        } else {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.EXPECTATION_FAILED).entity(null).build();
        }
    }

}