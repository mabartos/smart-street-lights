package org.smartlights.city.resources.providers;

import org.smartlights.city.dtos.CityDTO;
import org.smartlights.city.dtos.CitySerialiser;
import org.smartlights.city.resources.CitiesResource;
import org.smartlights.city.resources.CityResource;
import org.smartlights.city.resources.CitySession;
import org.smartlights.city.utils.Constants;
import static org.smartlights.city.dtos.CitySerialiser.entityToModel;
import static org.smartlights.city.dtos.CitySerialiser.modelToEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/cities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@RequestScoped
public class CitiesResourceProvider implements CitiesResource {

    @Inject
    CitySession session;

    @Path("{id}")
    public CityResource forwardToCity(@PathParam("id") Long cityID, CitySession session) {
        return new CityResourceProvider(session.setCityID(cityID));
    }

    @GET
    public Set<CityDTO> getAllCities(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                     @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getCityRepository().getAll(firstResult, maxResults)
                .map(CitySerialiser::entityToModel)
                .collect(Collectors.toSet());
    }

    @Override
    public CityDTO create(CityDTO city) {
        return entityToModel(session.getCityRepository().create(modelToEntity(city)));
    }

    @Override
    public CityDTO update(CityDTO city) {
        return entityToModel(session.getCityRepository().update(modelToEntity(city)));
    }
}
