package org.smartlights.device.resources.async;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.NeighborsRepository;
import org.smartlights.device.resources.DeviceSession;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NeighborsResourceAsync {

    private final Long deviceID;
    private final NeighborsRepository neighborsRepository;

    public NeighborsResourceAsync(DeviceSession session) {
        this.deviceID = session.getActualDeviceID();
        this.neighborsRepository = session.getNeighborsRepository();
    }

    @GET
    @Path("/id")
    public Multi<Long> getAllID() {
        return Multi.createFrom().items(neighborsRepository.getAllID(deviceID));
    }

    @GET
    public Multi<DeviceEntity> getAll() {
        return Multi.createFrom().items(neighborsRepository.getAll(deviceID));
    }

    @GET
    @Path("/count")
    public Uni<Integer> getCount() {
        return Uni.createFrom().item(neighborsRepository.getNeighborsCount(deviceID));
    }

    @GET
    @Path("/{id}")
    public Uni<DeviceEntity> getByID(@PathParam("id") Long id) {
        return Uni.createFrom()
                .item(neighborsRepository.getByID(deviceID, id))
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }

    @POST
    @Path("/{id}")
    public Uni<Boolean> addNeighbor(@PathParam("id") Long id) {
        return Uni.createFrom().item(neighborsRepository.addNeighbor(deviceID, id));
    }

    @DELETE
    @Path("/{id}")
    public Uni<Boolean> removeNeighbor(@PathParam("id") Long id) {
        return Uni.createFrom().item(neighborsRepository.removeNeighbor(deviceID, id));
    }
}