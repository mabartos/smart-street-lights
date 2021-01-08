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
import org.smartlights.device.resources.DeviceResource;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.DevicesResource;
import org.smartlights.device.utils.Constants;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@RequestScoped
@Path("devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class DevicesResourceProvider implements DevicesResource {

    @Inject
    DeviceSession session;

    @Inject
    DeviceSerializer serializer;

    @Path("{id}")
    public DeviceResource forwardToDevice(@PathParam("id") Long id) {
        return new DeviceResourceProvider(session.setActualDeviceID(id));
    }

    @GET
    @Path("serial/{serialNo}")
    @Timed(name = "getDeviceBySerialNo", description = "A measure of how long it takes to get device by serial No.")
    @Retry
    @Timeout
    public DeviceDTO getBySerialNo(@PathParam("serialNo") String serialNo) {
        return serializer.entityToModel(session.getDeviceRepository().getBySerialNo(serialNo));
    }

    @GET
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN})
    @Timed(name = "getAllDevices", description = "Get all devices", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Set<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDeviceRepository()
                .getAll(firstResult, maxResults)
                .map(serializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("fromStreet/{" + Constants.STREET_ID_PARAM + "}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllDevicesFromStreet", description = "Get all devices from street", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Set<DeviceDTO> getAllFromStreet(@PathParam(Constants.STREET_ID_PARAM) String streetID,
                                           @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                           @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDeviceRepository()
                .getAllFromStreet(streetID, firstResult, maxResults)
                .map(serializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("fromCity/{" + Constants.CITY_ID_PARAM + "}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllDevicesFromCity", description = "Get all devices from city", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Set<DeviceDTO> getAllFromCity(@PathParam(Constants.CITY_ID_PARAM) String cityID,
                                         @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                         @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return session.getDeviceRepository()
                .getAllFromCity(cityID, firstResult, maxResults)
                .map(serializer::entityToModel)
                .collect(Collectors.toSet());
    }

    @POST
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Counted(name = "createDeviceCount", description = "How many devices have been created.")
    @Timed(name = "createDeviceTime", description = "A measure of how long it takes to create a device.")
    @Timeout
    public DeviceDTO create(DeviceDTO device) {
        return Optional.ofNullable(session.getDeviceRepository().create(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .orElseThrow(notFoundException());
    }

    @PUT
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Counted(name = "updateDeviceCount", description = "How many devices have been updated.")
    @Timed(name = "updateDeviceTime", description = "A measure of how long it takes to update a device.")
    @Timeout
    public DeviceDTO update(DeviceDTO device) {
        return Optional.ofNullable(session.getDeviceRepository().update(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .orElseThrow(notFoundException());
    }
}
