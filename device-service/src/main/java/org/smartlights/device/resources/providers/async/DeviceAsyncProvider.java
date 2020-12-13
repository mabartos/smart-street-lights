package org.smartlights.device.resources.providers.async;

import io.smallrye.mutiny.Uni;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.async.DeviceResourceAsync;
import org.smartlights.device.resources.providers.DeviceResourceProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceAsyncProvider implements DeviceResourceAsync {

    private final DeviceSession session;
    private final Long id;
    private final DeviceSerializer serializer;

    public DeviceAsyncProvider(DeviceSession session) {
        this.session = session;
        this.id = session.getActualDeviceID();
        this.serializer = session.getDeviceSerializer();
    }

    @GET
    public Uni<DeviceDTO> getByID() {
        return Uni.createFrom()
                .item(session.getDeviceRepository().getByID(id))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull()
                .failWith(notFoundException(id));
    }

    @DELETE
    public Response delete() {
        return new DeviceResourceProvider(session).delete();
    }
}