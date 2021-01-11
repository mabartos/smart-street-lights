package org.smartlights.street.resources.providers;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.street.client.UserRole;
import org.smartlights.street.dtos.StreetDTO;
import org.smartlights.street.dtos.StreetSerialiser;
import org.smartlights.street.resources.StreetResource;
import org.smartlights.street.resources.StreetSession;
import org.smartlights.street.utils.Constants;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.stream.Collectors;

import static org.smartlights.street.dtos.StreetSerialiser.entityToModel;
import static org.smartlights.street.dtos.StreetSerialiser.modelToEntity;

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

    @GET
    @PermitAll
    @CircuitBreaker
    @Timed(name = "getAllStreets", description = "A measure of how long it takes to get a list of all streets.")
    public Set<StreetDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getStreetRepository().getAll(firstResult, maxResults)
                .map(StreetSerialiser::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("fromCity/{cityID}")
    @PermitAll
    @Timed(name = "getAllStreetsFromCity", description = "A measure of how long it takes to get all streets in a given city.")
    public Set<StreetDTO> getAllFromCity(@PathParam("cityID") String cityID,
                                         @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                         @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getStreetRepository()
                .getAllFromCity(cityID, firstResult, maxResults)
                .map(StreetSerialiser::entityToModel)
                .collect(Collectors.toSet());
    }

    @POST
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN})
    @Counted(name = "createStreetCount", description = "How many streets have been created.")
    @Timed(name = "createStreetTime", description = "A measure of how long it takes to create a street.")
    public StreetDTO create(StreetDTO street) {
        return entityToModel(session.getStreetRepository().create(modelToEntity(street)));
    }

    @PUT
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN})
    @Timed(name = "updateStreet", description = "A measure of how long it takes to update a street.")
    public StreetDTO update(StreetDTO street) {
        return entityToModel(session.getStreetRepository().update(modelToEntity(street)));
    }
}
