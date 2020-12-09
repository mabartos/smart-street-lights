package org.smartlights.device.resources.async;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.smartlights.device.entity.Device;
import org.smartlights.device.resources.DeviceSession;

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

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@ApplicationScoped
@Path("/devices/async")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DevicesResourceAsync {

    @Inject
    DeviceSession session;

    @GET
    public Multi<Device> getAll(@QueryParam("firstResult") Integer firstResult,
                                @QueryParam("maxResults") Integer maxResults) {
        return Multi.createFrom().items(session.getDeviceRepository().getAll(firstResult, maxResults));
    }

    @GET
    @Path("/serial/{serialNo}")
    public Uni<Device> getBySerialNo(@PathParam("serialNo") String serialNo) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().getBySerialNo(serialNo))
                .onItem()
                .ifNull().failWith(notFoundException());
    }

    // Lately, it'd be better from street service
    @GET
    @Path("/fromStreet/{streetID}")
    public Multi<Device> getAllFromStreet(@PathParam("streetID") String streetID,
                                          @QueryParam("firstResult") Integer firstResult,
                                          @QueryParam("maxResults") Integer maxResults) {
        return Multi.createFrom()
                .items(session.getDeviceRepository().getAllFromStreet(streetID, firstResult, maxResults));
    }

    @GET
    @Path("/fromCity/{cityID}")
    public Multi<Device> getAllFromCity(@PathParam("cityID") String cityID,
                                        @QueryParam("firstResult") Integer firstResult,
                                        @QueryParam("maxResults") Integer maxResults) {
        return Multi.createFrom()
                .items(session.getDeviceRepository().getAllFromCity(cityID, firstResult, maxResults));
    }

    @POST
    public Uni<Device> create(Device device) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().create(device))
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }

    @PUT
    public Uni<Device> update(Device device) {
        return Uni.createFrom()
                .item(session.getDeviceRepository().update(device))
                .onItem()
                .ifNull()
                .failWith(notFoundException());
    }
}
