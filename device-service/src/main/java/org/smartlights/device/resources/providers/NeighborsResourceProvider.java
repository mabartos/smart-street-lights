package org.smartlights.device.resources.providers;

import io.quarkus.security.Authenticated;
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
import org.smartlights.device.resources.NeighborsResource;
import org.smartlights.device.utils.Constants;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.smartlights.device.utils.DeviceErrorMessages.getNotFoundMessage;
import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class NeighborsResourceProvider implements NeighborsResource {

    private final Long deviceID;
    private final NeighborsRepository neighborsRepository;
    private final DeviceSerializer serializer;

    public NeighborsResourceProvider(DeviceSession session) {
        this.deviceID = session.getActualDeviceID();
        this.neighborsRepository = session.getNeighborsRepository();
        this.serializer = session.getDeviceSerializer();
    }

    @GET
    @Path("id")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllNeighborsID", description = "Get all neighbors IDs.", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Set<Long> getAllID(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                              @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return neighborsRepository.getAllID(deviceID, firstResult, maxResults)
                .collect(Collectors.toSet());
    }

    @GET
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllNeighbors", description = "Get all neighbors.", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Set<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return neighborsRepository.getAll(deviceID, firstResult, maxResults)
                .map(serializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("count")
    @Timed(name = "getNeighborsCountTime", description = "A measure of how long it takes to get neighbors count.")
    @Retry
    @Timeout
    public Integer getCount() {
        return neighborsRepository.getNeighborsCount(deviceID);
    }

    @GET
    @Path("{id}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getNeighborByIDTime", description = "A measure of how long it takes to get neighbor by ID.")
    @Retry
    @Timeout
    public DeviceDTO getByID(@PathParam("id") Long id) {
        return Optional.ofNullable(neighborsRepository.getByID(deviceID, id))
                .map(serializer::entityToModel)
                .orElseThrow(notFoundException());
    }

    @POST
    @Path("{id}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Counted(name = "addNeighborCount", description = "How many neighbors have been added.")
    @Timed(name = "addNeighborTime", description = "A measure of how long it takes to add neighbor.")
    @Timeout
    public Response addNeighbor(@PathParam("id") Long id) {
        if (!neighborsRepository.addNeighbor(deviceID, id)) {
            throw new NotFoundException(getNotFoundMessage());
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Counted(name = "removeNeighborCount", description = "How many neighbors have been removed.")
    @Timed(name = "removeNeighborTime", description = "A measure of how long it takes to remove neighbor.")
    @Timeout
    public Response removeNeighbor(@PathParam("id") Long id) {
        if (!neighborsRepository.removeNeighbor(deviceID, id)) {
            throw new NotFoundException(getNotFoundMessage());
        }
        return Response.ok().build();
    }
}