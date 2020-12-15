package org.smartlights.device.resources;

import org.smartlights.device.dto.DeviceDataDTO;
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
import java.sql.Timestamp;
import java.util.stream.Stream;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface DataResource {

    @POST
    Response saveData(DeviceDataDTO data);

    @GET
    Stream<DeviceDataDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("recent/{timestamp}")
    Stream<DeviceDataDTO> getAllRecentThan(@PathParam("timestamp") Timestamp timestamp,
                                           @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                           @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("recent/{timestamp}")
    Stream<DeviceDataDTO> getAllOlderThan(@PathParam("timestamp") Timestamp timestamp,
                                          @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                          @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @DELETE
    @Path("{timestamp}")
    boolean removeOlderThan(@PathParam("timestamp") Timestamp timestamp);
}
