package org.smartlights.device.resources.async;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.utils.Constants;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface NeighborsResourceAsync {

    @GET
    @Path("/id")
    Multi<Long> getAllID(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                         @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    Multi<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                            @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("/count")
    Uni<Integer> getCount();

    @GET
    @Path("/{id}")
    Uni<DeviceDTO> getByID(@PathParam("id") Long id);

    @POST
    @Path("/{id}")
    Uni<Boolean> addNeighbor(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    Uni<Boolean> removeNeighbor(@PathParam("id") Long id);
}