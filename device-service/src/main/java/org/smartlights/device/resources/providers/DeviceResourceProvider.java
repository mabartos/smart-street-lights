package org.smartlights.device.resources.providers;

import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.resources.DataResource;
import org.smartlights.device.resources.DeviceResource;
import org.smartlights.device.resources.DeviceSession;

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
public class DeviceResourceProvider implements DeviceResource {

    private final DeviceSession session;
    private final Long id;

    public DeviceResourceProvider(DeviceSession session) {
        this.session = session;
        this.id = session.getActualDeviceID();
    }

    @GET
    public DeviceDTO getByID() {
        return Optional.ofNullable(session.getDeviceRepository().getByID(id))
                .map(session.getDeviceSerializer()::entityToModel)
                .orElseThrow(notFoundException(id));
    }

    @DELETE
    public Response delete() {
        if (session.getDeviceRepository().deleteByID(id)) {
            return Response.ok().build();
        }
        throw new NotFoundException(getNotFoundMessage(id));
    }

    @GET
    @Path("/data/count")
    public int getCountOfData() {
        return session.getDeviceRepository().getCountOfData(id);
    }

    @Path("/data")
    public DataResource forwardToDataResource() {
        return new DataResourceProvider(session.setActualDeviceID(id));
    }
}
