package org.smartlights.simulation.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.smartlights.simulation.model.AuthUserDTO;
import org.smartlights.simulation.model.UserDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.smartlights.simulation.client.AuthTokenUtil.AUTHORIZATION_FIELD;
import static org.smartlights.simulation.client.AuthTokenUtil.AUTHORIZATION_METHOD_PATH;

/**
 * Remote User service
 */
@RegisterRestClient(configKey = "user-service")
@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserService {

    @POST
    @Path("users")
    UserDTO createUser(UserDTO user);

    @GET
    @Path("users/username/{username}")
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    UserDTO getByUsername(@PathParam("username") String username);

    @POST
    @Path("auth/token")
    @Produces(MediaType.TEXT_PLAIN)
    String getAccessToken(AuthUserDTO authUserDTO);
}
