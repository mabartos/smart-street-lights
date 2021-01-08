package org.smartlights.device.resources.providers;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.device.client.UserRole;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.resources.DeviceResource;
import org.smartlights.device.resources.DeviceSession;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.smartlights.device.utils.DeviceErrorMessages.getNotFoundMessage;
import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@RequestScoped
@Authenticated
public class DeviceResourceProvider implements DeviceResource {

    private final DeviceSession session;
    private final Long id;

    public DeviceResourceProvider(DeviceSession session) {
        this.session = session;
        this.id = session.getActualDeviceID();
    }

    @GET
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getDeviceByIdTime", description = "A measure of how long it takes to get device by ID.")
    @Retry
    @Timeout
    public DeviceDTO getByID() {
        return Optional.ofNullable(session.getDeviceRepository().getByID(id))
                .map(session.getDeviceSerializer()::entityToModel)
                .orElseThrow(notFoundException(id));
    }

    @DELETE
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Timed(name = "deleteDeviceTime", description = "A measure of how long it takes to remove device by ID.")
    @Timeout
    public Response delete() {
        if (session.getDeviceRepository().deleteByID(id)) {
            return Response.ok().build();
        }
        throw new NotFoundException(getNotFoundMessage(id));
    }

    @GET
    @Path("data/count")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getCountOfDataTime", description = "A measure of how long it takes to get remote device data.")
    @Retry
    @Timeout
    public Long getCountOfData() {
        return session.getDeviceDataService().countOfDeviceData(id);
    }
}
