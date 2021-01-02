package org.smartlights.user.resources;

import org.smartlights.user.data.UserDTO;
import org.smartlights.user.utils.Constants;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Transactional
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UsersResource {

    @GET
    Set<UserDTO> getAllUsers(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                             @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @POST
    UserDTO createUser(UserDTO user);

    @PATCH
    UserDTO updateUser(UserDTO user);

    @Path("{id}")
    UserResource forwardToUserResource(@PathParam("id") Long userID, UserSession session);
}
