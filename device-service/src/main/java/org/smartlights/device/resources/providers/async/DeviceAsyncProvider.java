package org.smartlights.device.resources.providers.async;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.device.client.UserRole;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.resources.async.DeviceResourceAsync;
import org.smartlights.device.resources.async.NeighborsResourceAsync;
import org.smartlights.device.resources.providers.DeviceResourceProvider;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Authenticated
public class DeviceAsyncProvider implements DeviceResourceAsync {

    private final DeviceSession session;
    private final Long id;
    private final DeviceSerializer serializer;

    public DeviceAsyncProvider(DeviceSession session) {
        this.session = session;
        this.id = session.getActualDeviceID();
        this.serializer = session.getDeviceSerializer();
    }

    @Path("neighbors")
    public NeighborsResourceAsync forwardToDevice() {
        return new NeighborsAsyncProvider(session);
    }

    @GET
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getDeviceByIdAsyncTime", description = "A measure of how long it takes to get asynchronously device by ID.")
    @Retry
    @Timeout
    public Uni<DeviceDTO> getByID() {
        return Uni.createFrom()
                .item(session.getDeviceRepository().getByID(id))
                .map(serializer::entityToModel)
                .onItem()
                .ifNull()
                .failWith(notFoundException(id));
    }

    @DELETE
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Timed(name = "deleteDeviceAsyncTime", description = "A measure of how long it takes to remove asynchronously device by ID.")
    @Timeout
    public Response delete() {
        return new DeviceResourceProvider(session).delete();
    }
}