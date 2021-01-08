package org.smartlights.device.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static org.smartlights.device.client.AuthTokenUtil.AUTHORIZATION_FIELD;
import static org.smartlights.device.client.AuthTokenUtil.AUTHORIZATION_METHOD_PATH;

@RegisterRestClient(configKey = "device-data-service")
@Path("/api/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeviceDataService {

    @DELETE
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    Response removeAllByID(Set<Long> ids);

    @GET
    @Path("device/{id}/count")
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    Long countOfDeviceData(@PathParam("id") Long id);

}
