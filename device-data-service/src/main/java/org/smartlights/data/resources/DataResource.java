package org.smartlights.data.resources;


import org.smartlights.data.dto.DeviceDataDTO;

import javax.enterprise.context.RequestScoped;
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
public interface DataResource {

    @GET
    @Path("{id}")
    DeviceDataDTO getByID(@PathParam("id") Long id);

    @POST
    Response handleData(DeviceDataDTO data);

    @DELETE
    @Path("{id}")
    Response removeByID(@PathParam("id") Long id);

    @Path("device/{id}")
    DataDeviceResource forwardToDeviceData(@PathParam("id") Long deviceID);
}
