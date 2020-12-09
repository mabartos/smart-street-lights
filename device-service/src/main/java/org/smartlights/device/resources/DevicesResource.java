package org.smartlights.device.resources;

import org.smartlights.device.entity.Device;

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
    public Stream<Device> getAll(@QueryParam("firstResult") Integer firstResult,
                                 @QueryParam("maxResults") Integer maxResults) {
        return session.getDeviceService().getAll(firstResult, maxResults);
    }

    @POST
    public void create(Device device) {
        session.getDeviceService().create(device);
    }

    @PUT
    public Device update(Device device) {
        return Optional.ofNullable(session.getDeviceService().update(device))
                .orElseThrow(notFoundException());
    }
}
