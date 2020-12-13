package org.smartlights.device.resources.async;

import io.smallrye.mutiny.Uni;
import org.smartlights.device.dto.DeviceDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DeviceResourceAsync {

    @GET
    Uni<DeviceDTO> getByID();

    @DELETE
    Response delete();
}