package org.smartlights.device.resources.providers.async;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.async.DeviceResourceAsync;
import org.smartlights.device.resources.async.DevicesResourceAsync;
import org.smartlights.device.utils.Constants;

import javax.enterprise.context.ApplicationScoped;
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

@ApplicationScoped
@Path("/async/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class DevicesAsyncProvider implements DevicesResourceAsync {

    @Inject
    DeviceSession session;

    @Inject
    DeviceSerializer serializer;

    @GET
    @Path("/{id}")
    public DeviceResourceAsync forwardToDevice(@PathParam("id") Long id) {
        return new DeviceAsyncProvider(session.setActualDeviceID(id));
    }

    @GET
    @Timed(name = "getAllDevicesAsync", description = "Get all devices async", unit = MetricUnits.MILLISECONDS)
    public Multi<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                   @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom().items(session.getDeviceRepository()
                .getAll(firstResult, maxResults)
                .map(serializer::entityToModel));
    }

    @GET
    @Path("/serial/{serialNo}")
    public Uni<DeviceDTO> getBySerialNo(@PathParam("serialNo") String serialNo) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().getBySerialNo(serialNo))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull().failWith(notFoundException());
    }

    @GET
    @Path("/fromStreet/{streetID}")
    public Multi<DeviceDTO> getAllFromStreet(@PathParam("streetID") String streetID,
                                             @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                             @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom()
                .items(session.getDeviceRepository()
                        .getAllFromStreet(streetID, firstResult, maxResults)
                        .map(serializer::entityToModel));
    }

    @GET
    @Path("/fromCity/{cityID}")
    public Multi<DeviceDTO> getAllFromCity(@PathParam("cityID") String cityID,
                                           @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                           @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return Multi.createFrom()
                .items(session.getDeviceRepository()
                        .getAllFromCity(cityID, firstResult, maxResults)
                        .map(serializer::entityToModel));
    }

    @POST
    public Uni<DeviceDTO> create(DeviceDTO device) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().create(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }

    @PUT
    public Uni<DeviceDTO> update(DeviceDTO device) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().update(serializer.modelToEntity(device)))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }
}
