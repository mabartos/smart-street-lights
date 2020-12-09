package org.smartlights.device.resources.async;

import io.smallrye.mutiny.Uni;
import org.smartlights.device.entity.Device;
import org.smartlights.device.resources.DeviceResource;
import org.smartlights.device.resources.DeviceSession;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResourceAsync {

    private DeviceSession session;
    private final Long id;

    public DeviceResourceAsync(DeviceSession session) {
        this.session = session;
        this.id = session.getActualDeviceID();
    }

    @GET
    public Uni<Device> getByID() {
        return Uni.createFrom()
                .item(session.getDeviceRepository().getByID(id))
                .onItem()
                .ifNull()
                .failWith(notFoundException(id));
    }

    @DELETE
    public Response delete() {
        return new DeviceResource(session).delete();
    }
}