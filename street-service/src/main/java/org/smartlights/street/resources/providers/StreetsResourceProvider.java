package org.smartlights.street.resources.providers;

import org.smartlights.street.dtos.StreetDTO;
import org.smartlights.street.dtos.StreetSerialiser;
import org.smartlights.street.resources.StreetResource;
import org.smartlights.street.resources.StreetSession;
import org.smartlights.street.resources.StreetsResource;
import org.smartlights.street.utils.Constants;
import static org.smartlights.street.dtos.StreetSerialiser.entityToModel;
import static org.smartlights.street.dtos.StreetSerialiser.modelToEntity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/streets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class StreetsResourceProvider implements StreetsResource {

    @Inject
    StreetSession session;

    @Path("{id}")
    public StreetResource forwardToStreet(@PathParam("id") Long id, StreetSession session) {
        return new StreetResourceProvider(session.setStreetID(id));
    }

    @Override
    public Set<StreetDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getStreetRepository().getAll(firstResult, maxResults)
                .map(StreetSerialiser::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("fromCity/{cityID}")
    public Set<StreetDTO> getAllFromCity(@PathParam("cityID") String cityID,
                                         @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                         @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getStreetRepository()
                .getAllFromCity(cityID, firstResult, maxResults)
                .map(StreetSerialiser::entityToModel)
                .collect(Collectors.toSet());
    }

    @Override
    public StreetDTO create(StreetDTO street) {
        return entityToModel(session.getStreetRepository().create(modelToEntity(street)));
    }

    @Override
    public StreetDTO update(StreetDTO street) {
        return entityToModel(session.getStreetRepository().update(modelToEntity(street)));
    }
}
