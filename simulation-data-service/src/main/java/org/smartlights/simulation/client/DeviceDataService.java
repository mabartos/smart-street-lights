package org.smartlights.simulation.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.smartlights.simulation.model.DeviceDataDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient(configKey = "device-data-service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/data")
public interface DeviceDataService {

    @POST
    @Path("/device/{id}")
    Response handleData(@PathParam("id") Long deviceID, DeviceDataDTO data);
}
