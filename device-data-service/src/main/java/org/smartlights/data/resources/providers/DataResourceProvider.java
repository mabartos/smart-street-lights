package org.smartlights.data.resources.providers;

import org.smartlights.data.dto.DataSerializer;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.resources.DataDeviceResource;
import org.smartlights.data.resources.DataResource;
import org.smartlights.data.resources.DataSession;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/data")
@Transactional
@RequestScoped
public class DataResourceProvider implements DataResource {

    @Inject
    DataSession session;

    @GET
    @Path("/{id}")
    public DeviceDataDTO getByID(@PathParam("id") Long id) {
        return DataSerializer.entityToModel(session.getDataRepository().getByID(id));
    }

    @POST
    public Response handleData(DeviceDataDTO data) {
        return session.getDataService().handleData(data) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeByID(@PathParam("id") Long id) {
        return session.getDataRepository().removeByID(id) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("/device/{id}")
    public DataDeviceResource forwardToDeviceData(@PathParam("id") Long deviceID) {
        return new DataDeviceProvider(session.setActualDevice(deviceID));
    }
}
