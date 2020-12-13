package org.smartlights.device.resources.providers.async;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.NeighborsRepository;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.async.NeighborsResourceAsync;
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

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
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
    @Path("/id")
    public Multi<Long> getAllID(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom().items(neighborsRepository.getAllID(deviceID, firstResult, maxResults));
    }

    @GET
    public Multi<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                   @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom().items(neighborsRepository
                .getAll(deviceID, firstResult, maxResults)
                .map(serializer::entityToModel));
    }

    @GET
    @Path("/count")
    public Uni<Integer> getCount() {
        return Uni.createFrom().item(neighborsRepository.getNeighborsCount(deviceID));
    }

    @GET
    @Path("/{id}")
    public Uni<DeviceDTO> getByID(@PathParam("id") Long id) {
        return Uni.createFrom()
                .item(neighborsRepository.getByID(deviceID, id))
                .map(serializer::entityToModel)
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