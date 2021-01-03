package org.smartlights.street.resources.providers;

import org.smartlights.street.dtos.StreetDTO;
import org.smartlights.street.resources.StreetResource;
import org.smartlights.street.resources.StreetSession;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
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

    @Override
    public StreetDTO getByID() {
        return entityToModel(session.getStreetRepository().getById(streetID));
    }

    @Override
    public boolean removeStreet() {
        return session.getStreetRepository().deleteStreetByID(streetID);
    }
}
