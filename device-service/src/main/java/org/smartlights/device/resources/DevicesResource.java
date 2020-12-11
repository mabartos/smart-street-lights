package org.smartlights.device.resources;

import org.smartlights.device.entity.DeviceEntity;

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
public class DevicesResource {

    @Inject
    DeviceSession session;

    @GET
    @Path("/{id}")
    public DeviceResource forwardToDevice(@PathParam("id") Long id) {
        return new DeviceResource(session.setActualDeviceID(id));
    }

    @GET
    @Path("/serial/{serialNo}")
    public DeviceEntity getBySerialNo(@PathParam("serialNo") String serialNo) {
        return session.getDeviceRepository().getBySerialNo(serialNo);
    }

    @GET
    public Stream<DeviceEntity> getAll(@QueryParam("firstResult") Integer firstResult,
                                       @QueryParam("maxResults") Integer maxResults) {
        return session.getDeviceRepository().getAll(firstResult, maxResults);
    }

    // Lately, it'd be better from street service
    @GET
    @Path("/fromStreet/{streetID}")
    public Stream<DeviceEntity> getAllFromStreet(@PathParam("streetID") String streetID,
                                                 @QueryParam("firstResult") Integer firstResult,
                                                 @QueryParam("maxResults") Integer maxResults) {
        return session.getDeviceRepository().getAllFromStreet(streetID, firstResult, maxResults);
    }

    @GET
    @Path("/fromCity/{cityID}")
    public Stream<DeviceEntity> getAllFromCity(@PathParam("cityID") String cityID,
                                               @QueryParam("firstResult") Integer firstResult,
                                               @QueryParam("maxResults") Integer maxResults) {
        return session.getDeviceRepository().getAllFromCity(cityID, firstResult, maxResults);
    }

    @POST
    public DeviceEntity create(DeviceEntity deviceEntity) {
        return Optional.ofNullable(session.getDeviceRepository().create(deviceEntity))
                .orElseThrow(notFoundException());
    }

    @PUT
    public DeviceEntity update(DeviceEntity deviceEntity) {
        return Optional.ofNullable(session.getDeviceRepository().update(deviceEntity))
                .orElseThrow(notFoundException());
    }
}
