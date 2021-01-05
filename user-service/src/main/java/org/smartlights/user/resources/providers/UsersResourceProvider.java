package org.smartlights.user.resources.providers;

import io.quarkus.security.Authenticated;
import org.smartlights.user.data.UserDTO;
import org.smartlights.user.data.UserRole;
import org.smartlights.user.data.UserSerializer;
import org.smartlights.user.resources.UserResource;
import org.smartlights.user.resources.UserSession;
import org.smartlights.user.resources.UsersResource;
import org.smartlights.user.utils.Constants;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import java.util.stream.Collectors;

import static org.smartlights.user.data.UserSerializer.entityToModel;
import static org.smartlights.user.data.UserSerializer.modelToEntity;

@Transactional
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Authenticated
public class UsersResourceProvider implements UsersResource {

    @Inject
    UserSession session;

    @GET
    @RolesAllowed({UserRole.ADMIN})
    public Set<UserDTO> getAllUsers(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                    @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getUserRepository().getAll(firstResult, maxResults)
                .map(UserSerializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @PermitAll
    @Path("username/{username}")
    public UserDTO getByUsername(@PathParam("username") String username) {
        return entityToModel(session.getUserRepository().getByUsername(username));
    }

    @POST
    @PermitAll
    public UserDTO createUser(UserDTO user) {
        return entityToModel(session.getUserRepository().create(modelToEntity(user)));
    }

    @PATCH
    @RolesAllowed({UserRole.ADMIN})
    public UserDTO updateUser(UserDTO user) {
        return entityToModel(session.getUserRepository().update(modelToEntity(user)));
    }

    @Path("{id}")
    public UserResource forwardToUserResource(@PathParam("id") Long userID, UserSession session) {
        return new UserResourceProvider(session.setUserID(userID));
    }
}
