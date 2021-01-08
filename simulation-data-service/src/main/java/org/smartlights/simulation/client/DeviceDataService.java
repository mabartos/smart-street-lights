package org.smartlights.simulation.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.smartlights.simulation.model.DeviceDataDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.smartlights.simulation.client.AuthTokenUtil.AUTHORIZATION_FIELD;
import static org.smartlights.simulation.client.AuthTokenUtil.AUTHORIZATION_METHOD_PATH;

/**
 * Remote Device Data service
 */
@RegisterRestClient(configKey = "device-data-service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/data")
public interface DeviceDataService {

    @POST
    @Path("device/{id}")
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    Response handleData(@PathParam("id") Long deviceID, DeviceDataDTO data);

    @GET
    @Path("test")
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    String test();
}
