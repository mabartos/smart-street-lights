package org.smartlights.user.resources.providers;

import org.smartlights.user.data.UserDTO;
import org.smartlights.user.resources.UserResource;
import org.smartlights.user.resources.UserSession;

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
public class UserResourceProvider implements UserResource {

    private final UserSession session;
    private final Long userID;

    public UserResourceProvider(UserSession session) {
        this.session = session;
        this.userID = session.getUserID();
    }

    @GET
    public UserDTO getUserByID() {
        return entityToModel(session.getUserRepository().getByID(userID));
    }

    @DELETE
    public boolean removeUser() {
        return session.getUserRepository().removeByID(userID);
    }
}
