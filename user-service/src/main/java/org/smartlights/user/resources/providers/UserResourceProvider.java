package org.smartlights.user.resources.providers;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.user.data.UserDTO;
import org.smartlights.user.data.UserRole;
import org.smartlights.user.resources.UserResource;
import org.smartlights.user.resources.UserSession;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.smartlights.user.data.UserSerializer.entityToModel;

@Transactional
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class UserResourceProvider implements UserResource {

    private final UserSession session;
    private final Long userID;

    public UserResourceProvider(UserSession session) {
        this.session = session;
        this.userID = session.getUserID();
    }

    @GET
    @Timed(name = "getUserByID", description = "A measure of how long it takes to get user by ID.")
    @Retry
    @Timeout
    public UserDTO getUserByID() {
        return entityToModel(session.getUserRepository().getByID(userID));
    }

    @DELETE
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Counted(name = "removeUserCount", description = "How many users were removed.")
    @Timed(name = "removeUserTime", description = "A measure of how long it takes to remove User.")
    @Retry
    @Timeout
    public boolean removeUser() {
        return session.getUserRepository().removeByID(userID);
    }
}
