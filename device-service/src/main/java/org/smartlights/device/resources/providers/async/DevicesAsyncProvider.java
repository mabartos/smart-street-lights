package org.smartlights.device.resources.providers.async;

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
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.async.DeviceResourceAsync;
import org.smartlights.device.resources.async.DevicesResourceAsync;
import org.smartlights.device.utils.Constants;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@RequestScoped
@Path("/devices/async")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class DevicesAsyncProvider implements DevicesResourceAsync {

    @Inject
    DeviceSession session;

    @Inject
    DeviceSerializer serializer;

    @Path("/{id}")
    public DeviceResourceAsync forwardToDevice(@PathParam("id") Long id) {
        return new DeviceAsyncProvider(session.setActualDeviceID(id));
    }

    @GET
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN})
    @Timed(name = "getAllDevicesAsync", description = "Get all devices asynchronously.", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Multi<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                   @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom().items(session.getDeviceRepository()
                .getAll(firstResult, maxResults)
                .map(serializer::entityToModel));
    }

    @GET
    @Path("/serial/{serialNo}")
    @Timed(name = "getDeviceBySerialAsyncNo", description = "A measure of how long it takes to get device by serial number asynchronously.")
    @Retry
    @Timeout
    public Uni<DeviceDTO> getBySerialNo(@PathParam("serialNo") String serialNo) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().getBySerialNo(serialNo))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull().failWith(notFoundException());
    }

    @GET
    @Path("/fromStreet/{" + Constants.STREET_ID_PARAM + "}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllDevicesFromStreetAsyncTime", description = "Get all devices from street asynchronously.", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Multi<DeviceDTO> getAllFromStreet(@PathParam(Constants.STREET_ID_PARAM) String streetID,
                                             @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                             @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom()
                .items(session.getDeviceRepository()
                        .getAllFromStreet(streetID, firstResult, maxResults)
                        .map(serializer::entityToModel));
    }

    @GET
    @Path("/fromCity/{" + Constants.CITY_ID_PARAM + "}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getAllDevicesFromCityAsyncTime", description = "Get all devices from city asynchronously.", unit = MetricUnits.MILLISECONDS)
    @CircuitBreaker
    @Timeout
    public Multi<DeviceDTO> getAllFromCity(@PathParam(Constants.CITY_ID_PARAM) String cityID,
                                           @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                           @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom()
                .items(session.getDeviceRepository()
                        .getAllFromCity(cityID, firstResult, maxResults)
                        .map(serializer::entityToModel));
    }

    @POST
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Counted(name = "createDeviceAsyncCount", description = "How many devices have been created asynchronously.")
    @Timed(name = "createDeviceAsyncTime", description = "A measure of how long it takes to create a device asynchronously.")
    @Timeout
    public Uni<DeviceDTO> create(DeviceDTO device) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().create(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }

    @PUT
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Counted(name = "updateDeviceAsyncCount", description = "How many devices have been updated asynchronously.")
    @Timed(name = "updateDeviceAsyncTime", description = "A measure of how long it takes to update a device asynchronously.")
    @Timeout
    public Uni<DeviceDTO> update(DeviceDTO device) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().update(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }
}
