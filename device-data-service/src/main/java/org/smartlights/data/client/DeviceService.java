package org.smartlights.data.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.smartlights.data.client.AuthTokenUtil.AUTHORIZATION_FIELD;
import static org.smartlights.data.client.AuthTokenUtil.AUTHORIZATION_METHOD_PATH;

/**
 * Remote device service
 */
@RegisterRestClient(configKey = "device-service")
@Path("/api/devices")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeviceService {

    @GET
    @Path("{id}")
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    DeviceDTO getByID(@PathParam("id") Long id);
}
