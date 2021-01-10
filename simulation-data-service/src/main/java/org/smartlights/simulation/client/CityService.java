package org.smartlights.simulation.client;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.smartlights.simulation.model.CityDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Set;

@RegisterRestClient(configKey = "city-service")
@Path("/api/cities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CityService {

    @GET
    @Fallback(fallbackMethod = "fallbackAllCities")
    Set<CityDTO> getAllCities(@QueryParam("firstResult") Integer firstResult,
                              @QueryParam("maxResults") Integer maxResults);

    @POST
    CityDTO create(CityDTO city);

    @GET
    @Path("{id}")
    CityDTO getByID(@PathParam("id") Long id);

    default Set<CityDTO> fallbackAllCities(Integer firstResult, Integer maxResults) {
        return Collections.emptySet();
    }
}
