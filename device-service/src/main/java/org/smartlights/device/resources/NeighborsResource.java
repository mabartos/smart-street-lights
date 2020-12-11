package org.smartlights.device.resources;

import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.NeighborsRepository;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.stream.Stream;

import static org.smartlights.device.utils.DeviceErrorMessages.getNotFoundMessage;
import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NeighborsResource {

    private final Long deviceID;
    private final NeighborsRepository neighborsRepository;

    public NeighborsResource(DeviceSession session) {
        this.deviceID = session.getActualDeviceID();
        this.neighborsRepository = session.getNeighborsRepository();
    }

    @GET
    @Path("/id")
    public Stream<Long> getAllID() {
        return Optional.ofNullable(neighborsRepository.getAllID(deviceID))
                .orElseGet(Stream::empty);
    }

    @GET
    public Stream<DeviceEntity> getAll() {
        return Optional.ofNullable(neighborsRepository.getAll(deviceID))
                .orElseGet(Stream::empty);
    }

    @GET
    @Path("/count")
    public Integer getCount() {
        return neighborsRepository.getNeighborsCount(deviceID);
    }

    @GET
    @Path("/{id}")
    public DeviceEntity getByID(@PathParam("id") Long id) {
        return Optional.ofNullable(neighborsRepository.getByID(deviceID, id))
                .orElseThrow(notFoundException());
    }

    @POST
    @Path("/{id}")
    public Response addNeighbor(@PathParam("id") Long id) {
        if (!neighborsRepository.addNeighbor(deviceID, id)) {
            throw new NotFoundException(getNotFoundMessage());
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeNeighbor(@PathParam("id") Long id) {
        if (!neighborsRepository.removeNeighbor(deviceID, id)) {
            throw new NotFoundException(getNotFoundMessage());
        }
        return Response.ok().build();
    }
}
