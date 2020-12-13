package org.smartlights.device.resources.async;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.utils.Constants;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/devices/async")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DevicesResourceAsync {

    @GET
    Multi<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                            @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("/serial/{serialNo}")
    Uni<DeviceDTO> getBySerialNo(@PathParam("serialNo") String serialNo);

    @GET
    @Path("/fromStreet/{streetID}")
    Multi<DeviceDTO> getAllFromStreet(@PathParam("streetID") String streetID,
                                      @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                      @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("/fromCity/{cityID}")
    Multi<DeviceDTO> getAllFromCity(@PathParam("cityID") String cityID,
                                    @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                    @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @POST
    Uni<DeviceDTO> create(DeviceDTO device);

    @PUT
    Uni<DeviceDTO> update(DeviceDTO device);
}
