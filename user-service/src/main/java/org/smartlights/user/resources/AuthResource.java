package org.smartlights.user.resources;

import org.smartlights.user.data.AuthUserDTO;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Transactional
@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthResource {

    @POST
    @Path("token")
    String getAccessToken(AuthUserDTO authUserDTO);
}
