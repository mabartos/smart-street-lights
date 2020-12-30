package org.smartlights.data.resources.providers;

import org.smartlights.data.dto.DataSerializer;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.resources.DataDeviceResource;
import org.smartlights.data.resources.DataSession;
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
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class DataDeviceProvider implements DataDeviceResource {

    private final DataSession session;
    private final Long deviceID;

    public DataDeviceProvider(DataSession session) {
        this.session = session;
        this.deviceID = session.getActualDeviceID();
    }

    @POST
    public Response handleData(Long deviceID, DeviceDataDTO data) {
        return session.getDataService().handleData(deviceID, data) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    public Set<DeviceDataDTO> getAllFromDevice(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                               @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDataRepository().getAllFromDevice(deviceID, firstResult, maxResults)
                .map(DataSerializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("recent/{timestamp}")
    public Set<DeviceDataDTO> getAllRecentThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                               @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                               @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDataRepository().getAllRecentThan(deviceID, timestamp, firstResult, maxResults)
                .map(DataSerializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("recent/{timestamp}")
    public Set<DeviceDataDTO> getAllOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                              @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                              @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDataRepository().getAllOlderThan(deviceID, timestamp, firstResult, maxResults)
                .map(DataSerializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @DELETE
    @Path("{timestamp}")
    public Response removeOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp) {
        return session.getDataRepository().removeOlderThan(deviceID, timestamp) ?
                Response.ok().build() :
                Response.status(Response.Status.BAD_REQUEST).build();
    }
}
