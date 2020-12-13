package org.smartlights.device.resources;

import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.utils.Constants;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Stream;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface NeighborsResource {

    @GET
    @Path("/id")
    Stream<Long> getAllID(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                          @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    Stream<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                             @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("/count")
    Integer getCount();

    @GET
    @Path("/{id}")
    DeviceDTO getByID(@PathParam("id") Long id);

    @POST
    @Path("/{id}")
    Response addNeighbor(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    Response removeNeighbor(@PathParam("id") Long id);
}
