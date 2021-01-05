package org.smartlights.data.resources.providers;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.data.dto.DataSerializer;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.entity.DeviceDataEntity;
import org.smartlights.data.resources.DataDeviceResource;
import org.smartlights.data.resources.DataResource;
import org.smartlights.data.resources.DataSession;

import javax.annotation.security.PermitAll;
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
    public DeviceDataDTO getByID(@PathParam("id") Long id) {
        DeviceDataEntity entity = session.getDataRepository().getByID(id);
        return DataSerializer.entityToModel(Optional.ofNullable(entity)
                .orElseThrow(() -> new NotFoundException("Not found data with specified ID")));
    }

    @POST
    @Timed(name = "handleOnlyData", description = "Handle only data SYNC", unit = MetricUnits.MILLISECONDS)
    public Response handleData(DeviceDataDTO data) {
        return session.getDataService().handleData(data) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("{id}")
    public Response removeByID(@PathParam("id") Long id) {
        return session.getDataRepository().removeByID(id) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    public Response removeAllByID(Set<Long> ids) {
        return session.getDataRepository().removeByIDs(ids) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("device/{id}")
    public DataDeviceResource forwardToDeviceData(@PathParam("id") Long deviceID) {
        return new DataDeviceProvider(session.setActualDevice(deviceID));
    }
}
