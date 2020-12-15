package org.smartlights.device.resources.providers;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.resources.DeviceResource;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.DevicesResource;
import org.smartlights.device.utils.Constants;
import org.smartlights.device.utils.DeviceType;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.stream.Stream;

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@ApplicationScoped
@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DevicesResourceProvider implements DevicesResource {

    @Inject
    DeviceSession session;

    @Inject
    DeviceSerializer serializer;

    @GET
    @Path("/{id}")
    public DeviceResource forwardToDevice(@PathParam("id") Long id) {
        return new DeviceResourceProvider(session.setActualDeviceID(id));
    }

    @GET
    @Path("/serial/{serialNo}")
    public DeviceDTO getBySerialNo(@PathParam("serialNo") String serialNo) {
        return serializer.entityToModel(session.getDeviceRepository().getBySerialNo(serialNo));
    }

    @GET
    @Timed(name = "getAllDevices", description = "Get all devices", unit = MetricUnits.MILLISECONDS)
    public Stream<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                    @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDeviceRepository()
                .getAll(firstResult, maxResults)
                .map(serializer::entityToModel);
    }

    @GET
    @Path("/fromStreet/{streetID}")
    public Stream<DeviceDTO> getAllFromStreet(@PathParam("streetID") String streetID,
                                              @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                              @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDeviceRepository()
                .getAllFromStreet(streetID, firstResult, maxResults)
                .map(serializer::entityToModel);
    }

    @GET
    @Path("/fromCity/{cityID}")
    public Stream<DeviceDTO> getAllFromCity(@PathParam("cityID") String cityID,
                                            @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                            @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDeviceRepository()
                .getAllFromCity(cityID, firstResult, maxResults)
                .map(serializer::entityToModel);
    }

    @POST
    public DeviceDTO create(DeviceDTO device) {
        return Optional.ofNullable(session.getDeviceRepository().create(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .orElseThrow(notFoundException());
    }

    @PUT
    public DeviceDTO update(DeviceDTO device) {
        return Optional.ofNullable(session.getDeviceRepository().update(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .orElseThrow(notFoundException());
    }
}
