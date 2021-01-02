package org.smartlights.data.resources;

import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.utils.Constants;

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
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface DataDeviceResource {

    @POST
    Response handleData(DeviceDataDTO data);

    @GET
    Long countOfDeviceData();

    @GET
    Set<DeviceDataDTO> getAllFromDevice(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                        @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("recent/{timestamp}")
    Set<DeviceDataDTO> getAllRecentThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                        @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                        @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("recent/{timestamp}")
    Set<DeviceDataDTO> getAllOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                       @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                       @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @DELETE
    @Path("{timestamp}")
    Response removeOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp);
}
