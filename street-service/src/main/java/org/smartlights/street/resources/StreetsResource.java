package org.smartlights.street.resources;

import org.smartlights.street.dtos.StreetDTO;
import org.smartlights.street.utils.Constants;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/streets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface StreetsResource {

    @Path("/{id}")
    StreetResource forwardToStreet(@PathParam("id") Long id, StreetSession session);

    @GET
    Set<StreetDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                          @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("/fromCity/{cityID}")
    Set<StreetDTO> getAllFromCity(@PathParam("cityID") String cityID,
                                  @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                  @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @POST
    StreetDTO create(StreetDTO street);

    @PUT
    StreetDTO update(StreetDTO street);

}
