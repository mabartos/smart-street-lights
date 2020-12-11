package org.smartlights.device.resources;

import org.smartlights.device.entity.DeviceEntity;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.smartlights.device.utils.DeviceErrorMessages.getNotFoundMessage;
import static org.smartlights.device.utils.DeviceErrorMessages.notFoundException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

    private final DeviceSession session;
    private final Long id;

    public DeviceResource(DeviceSession session) {
        this.session = session;
        this.id = session.getActualDeviceID();
    }

    @GET
    public DeviceEntity getByID() {
        return Optional.ofNullable(session.getDeviceRepository().getByID(id))
                .orElseThrow(notFoundException(id));
    }

    @DELETE
    public Response delete() {
        if (session.getDeviceRepository().deleteByID(id)) {
            return Response.ok().build();
        }
        throw new NotFoundException(getNotFoundMessage(id));
    }



}
