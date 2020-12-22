package org.smartlights.device.resources.providers;

import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.repository.NeighborsRepository;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.NeighborsResource;
import org.smartlights.device.utils.Constants;

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
    @Path("/id")
    public Set<Long> getAllID(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                              @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return neighborsRepository.getAllID(deviceID, firstResult, maxResults)
                .collect(Collectors.toSet());
    }

    @GET
    public Set<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return neighborsRepository.getAll(deviceID, firstResult, maxResults)
                .map(serializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("/count")
    public Integer getCount() {
        return neighborsRepository.getNeighborsCount(deviceID);
    }

    @GET
    @Path("/{id}")
    public DeviceDTO getByID(@PathParam("id") Long id) {
        return Optional.ofNullable(neighborsRepository.getByID(deviceID, id))
                .map(serializer::entityToModel)
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