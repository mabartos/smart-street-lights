package org.smartlights.city.resources;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import org.smartlights.city.dtos.CityDTO;
import org.smartlights.city.utils.Constants;

@Path("/cities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface CitiesResource {

    @Path("/{id}")
    CityResource forwardToCity(@PathParam("id") Long id);

    @GET
    Set<CityDTO> getAllCities(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                              @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @POST
    CityDTO create(CityDTO city);

    @PUT
    CityDTO update(CityDTO city);
}
