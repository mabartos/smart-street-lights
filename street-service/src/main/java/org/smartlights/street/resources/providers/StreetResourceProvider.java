package org.smartlights.street.resources.providers;

import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.street.dtos.StreetDTO;
import org.smartlights.street.resources.StreetResource;
import org.smartlights.street.resources.StreetSession;
import org.smartlights.street.client.UserRole;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.smartlights.street.dtos.StreetSerialiser.entityToModel;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class StreetResourceProvider implements StreetResource {

    private final StreetSession session;
    private final Long streetID;

    public StreetResourceProvider(StreetSession session) {
        this.session = session;
        this.streetID = session.getStreetID();
    }

    @GET
    @Timed(name = "getStreetByIdTime", description = "A measure of how long it takes to get street by ID.")
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN, UserRole.MAINTAINER})
    public StreetDTO getByID() {
        return entityToModel(session.getStreetRepository().getById(streetID));
    }

    @DELETE
    @Timed(name = "removeStreet", description = "A measure of how long it takes to delete street by ID.")
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN})
    public boolean removeStreet() {
        return session.getStreetRepository().deleteStreetByID(streetID);
    }
}
