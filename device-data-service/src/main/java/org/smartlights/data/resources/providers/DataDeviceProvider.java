package org.smartlights.data.resources.providers;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.data.dto.DataSerializer;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.resources.DataDeviceResource;
import org.smartlights.data.resources.DataSession;
import org.smartlights.data.utils.Constants;

import javax.annotation.security.RolesAllowed;
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

import static org.smartlights.data.client.UserRole.ADMIN;
import static org.smartlights.data.client.UserRole.DEVICE;
import static org.smartlights.data.client.UserRole.MAINTAINER;
import static org.smartlights.data.client.UserRole.SYS_ADMIN;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Authenticated
public class DataDeviceProvider implements DataDeviceResource {

    private final DataSession session;
    private final Long deviceID;

    public DataDeviceProvider(DataSession session) {
        this.session = session;
        this.deviceID = session.getActualDeviceID();
    }

    @POST
    @RolesAllowed({DEVICE})
    @Counted(name = "handleDataCount", description = "Handle data.")
    @Timed(name = "handleDataTime", description = "A measure of how long it takes to handle data.")
    @Timeout
    public Response handleData(DeviceDataDTO data) {
        return session.getDataService().handleData(deviceID, data) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("async")
    @RolesAllowed({DEVICE})
    @Counted(name = "handleDataAsyncCount", description = "Handle async data.")
    @Timed(name = "handleDataAsyncTime", description = "A measure of how long it takes to asynchronously handle data.")
    @Timeout
    public Uni<Response> handleDataAsync(DeviceDataDTO data) {
        return Uni.createFrom().item(handleData(data));
    }

    @GET
    @Path("count")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Timed(name = "countOfDeviceDataTime", description = "A measure of how long it takes to get count of device data.")
    @Timeout
    @Retry
    public Long countOfDeviceData() {
        return session.getDataRepository().getCountOfDeviceData(deviceID);
    }

    @GET
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Timed(name = "getAllFromDeviceTime", description = "A measure of how long it takes to get all data from device.")
    @CircuitBreaker
    @Timeout
    @Retry
    public Set<DeviceDataDTO> getAllFromDevice(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                               @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDataRepository().getAllFromDevice(deviceID, firstResult, maxResults)
                .map(DataSerializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("recent/{timestamp}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Timed(name = "getAllRecentDeviceTime", description = "A measure of how long it takes to get all data recent than timestamp from device.")
    @CircuitBreaker
    @Timeout
    @Retry
    public Set<DeviceDataDTO> getAllRecentThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                               @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                               @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDataRepository().getAllRecentThan(deviceID, timestamp, firstResult, maxResults)
                .map(DataSerializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("recent/{timestamp}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Timed(name = "getAllOlderDeviceTime", description = "A measure of how long it takes to get all data older than timestamp from device.")
    @CircuitBreaker
    @Timeout
    @Retry
    public Set<DeviceDataDTO> getAllOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                              @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                              @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDataRepository().getAllOlderThan(deviceID, timestamp, firstResult, maxResults)
                .map(DataSerializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @DELETE
    @Path("{timestamp}")
    @RolesAllowed({SYS_ADMIN, ADMIN})
    @Counted(name = "removeOlderCount", description = "How many data older than timestamp has been removed.")
    @Timed(name = "removeOlderTime", description = "A measure of how long it takes to remove data older than timestamp.")
    @Timeout
    @Retry
    public Response removeOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp) {
        return session.getDataRepository().removeOlderThan(deviceID, timestamp) ?
                Response.ok().build() :
                Response.status(Response.Status.BAD_REQUEST).build();
    }
}
