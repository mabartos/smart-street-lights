package org.smartlights.device.resources.providers.async;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.device.client.UserRole;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.repository.NeighborsRepository;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.async.NeighborsResourceAsync;
import org.smartlights.device.utils.Constants;

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

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Authenticated
public class NeighborsAsyncProvider implements NeighborsResourceAsync {

    private final Long deviceID;
    private final NeighborsRepository neighborsRepository;
    private final DeviceSerializer serializer;

    public NeighborsAsyncProvider(DeviceSession session) {
        this.deviceID = session.getActualDeviceID();
        this.neighborsRepository = session.getNeighborsRepository();
        this.serializer = session.getDeviceSerializer();
    }

    @GET
    @Path("id")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllNeighborsAsyncID", description = "Get all neighbors IDs asynchronously.", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Multi<Long> getAllID(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom().items(neighborsRepository.getAllID(deviceID, firstResult, maxResults));
    }

    @GET
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllNeighborsAsync", description = "Get all neighbors asynchronously.", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Multi<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                   @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom().items(neighborsRepository
                .getAll(deviceID, firstResult, maxResults)
                .map(serializer::entityToModel));
    }

    @GET
    @Path("count")
    @Timed(name = "getNeighborsCountAsyncTime", description = "A measure of how long it takes to get neighbors count asynchronously.")
    @Retry
    @Timeout
    public Uni<Integer> getCount() {
        return Uni.createFrom().item(neighborsRepository.getNeighborsCount(deviceID));
    }

    @GET
    @Path("{id}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getNeighborByIDAsyncTime", description = "A measure of how long it takes to get neighbor by ID asynchronously.")
    @Retry
    @Timeout
    public Uni<DeviceDTO> getByID(@PathParam("id") Long id) {
        return Uni.createFrom()
                .item(neighborsRepository.getByID(deviceID, id))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }

    @POST
    @Path("{id}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Counted(name = "addNeighborAsyncCount", description = "How many neighbors have been added asynchronously.")
    @Timed(name = "addNeighborAsyncTime", description = "A measure of how long it takes to add neighbor asynchronously.")
    @Timeout
    public Uni<Boolean> addNeighbor(@PathParam("id") Long id) {
        return Uni.createFrom().item(neighborsRepository.addNeighbor(deviceID, id));
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Counted(name = "removeNeighborAsyncCount", description = "How many neighbors have been removed asynchronously.")
    @Timed(name = "removeNeighborAsyncTime", description = "A measure of how long it takes to remove neighbor asynchronously.")
    @Timeout
    public Uni<Boolean> removeNeighbor(@PathParam("id") Long id) {
        return Uni.createFrom().item(neighborsRepository.removeNeighbor(deviceID, id));
    }
}