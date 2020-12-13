package org.smartlights.device.resources;

import org.smartlights.device.dto.DeviceDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DeviceResource {

    @GET
    DeviceDTO getByID();

    @DELETE
    Response delete();
}
