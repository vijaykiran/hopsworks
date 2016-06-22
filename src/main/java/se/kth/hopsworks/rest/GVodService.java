/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.rest;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import se.kth.hopsworks.certificates.UserCerts;
import se.kth.hopsworks.certificates.UserCertsFacade;
import se.kth.hopsworks.controller.UsersController;

import se.kth.hopsworks.filters.AllowedRoles;

/**
 *
 * @author jsvhqr
 */
@Path("/gvod")
@RolesAllowed({"HOPS_ADMIN", "HOPS_USER"})
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@TransactionAttribute(TransactionAttributeType.NEVER)
public class GVodService {
   
    private UsersController usersController;
    
    @EJB
    UserCertsFacade userCertsFacade;
    
    @GET
    @Path("kafkacerts")
    @Produces(MediaType.APPLICATION_JSON)
    @AllowedRoles(roles = {AllowedRoles.DATA_SCIENTIST, AllowedRoles.DATA_OWNER})
    public Response addCertsToHdfs(@Context SecurityContext sc,
    @Context HttpServletRequest req){
        
        return null;
        
    }
    
    
}
