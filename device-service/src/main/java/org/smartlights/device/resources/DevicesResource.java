package org.smartlights.device.resources;

import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.utils.Constants;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.stream.Stream;

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface DevicesResource {

    @Path("/{id}")
    DeviceResource forwardToDevice(@PathParam("id") Long id);

    @GET
    @Path("/serial/{serialNo}")
    DeviceDTO getBySerialNo(@PathParam("serialNo") String serialNo);

    @GET
    Stream<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("/fromStreet/{streetID}")
    Stream<DeviceDTO> getAllFromStreet(@PathParam("streetID") String streetID,
                                          @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                          @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);
    @GET
    @Path("/fromCity/{cityID}")
    Stream<DeviceDTO> getAllFromCity(@PathParam("cityID") String cityID,
                                        @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                        @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @POST
    DeviceDTO create(DeviceDTO device);

    @PUT
    DeviceDTO update(DeviceDTO device);
}
