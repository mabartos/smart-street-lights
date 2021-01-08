package org.smartlights.data.resources.providers;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.data.dto.DataSerializer;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.entity.DeviceDataEntity;
import org.smartlights.data.resources.DataDeviceResource;
import org.smartlights.data.resources.DataResource;
import org.smartlights.data.resources.DataSession;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;

import static org.smartlights.data.client.UserRole.ADMIN;
import static org.smartlights.data.client.UserRole.DEVICE;
import static org.smartlights.data.client.UserRole.MAINTAINER;
import static org.smartlights.data.client.UserRole.SYS_ADMIN;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/data")
@Transactional
@RequestScoped
public class DataResourceProvider implements DataResource {

    @Inject
    DataSession session;

    @Inject
    JsonWebToken token;

    @GET
    @Path("test")
    @RolesAllowed(ADMIN)
    public String test() {
        return Optional.ofNullable(token.getSubject()).orElse("nothing");
    }

    @GET
    @Path("{id}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Counted(name = "getDataByIDCount", description = "Get data by ID.")
    @Timed(name = "getDataByIDTime", description = "A measure of how long it takes to get data by ID.")
    @Timeout
    @Retry
    public DeviceDataDTO getByID(@PathParam("id") Long id) {
        DeviceDataEntity entity = session.getDataRepository().getByID(id);
        return DataSerializer.entityToModel(Optional.ofNullable(entity)
                .orElseThrow(() -> new NotFoundException("Not found data with specified ID")));
    }

    @POST
    @RolesAllowed({DEVICE})
    @Counted(name = "handleOnlyDataCount", description = "Handle data.")
    @Timed(name = "handleOnlyDataTime", description = "A measure of how long it takes to handle data.")
    @Timeout
    public Response handleData(DeviceDataDTO data) {
        return session.getDataService().handleData(data) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Counted(name = "removeDataByIDCount", description = "Count of removed data.")
    @Timed(name = "removeDataByIDTime", description = "A measure of how long it takes to remove data by ID.")
    @Timeout
    public Response removeByID(@PathParam("id") Long id) {
        return session.getDataRepository().removeByID(id) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Counted(name = "removeAllDataByIDCount", description = "Count of removed data.")
    @Timed(name = "removeAllDataByIDTime", description = "A measure of how long it takes to remove data by IDs.")
    @Timeout
    public Response removeAllByID(Set<Long> ids) {
        return session.getDataRepository().removeByIDs(ids) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("device/{id}")
    public DataDeviceResource forwardToDeviceData(@PathParam("id") Long deviceID) {
        return new DataDeviceProvider(session.setActualDevice(deviceID));
    }
}
