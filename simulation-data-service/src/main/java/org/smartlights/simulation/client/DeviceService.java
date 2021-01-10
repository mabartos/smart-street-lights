package org.smartlights.simulation.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.smartlights.simulation.model.DeviceDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.smartlights.simulation.client.AuthTokenUtil.AUTHORIZATION_FIELD;
import static org.smartlights.simulation.client.AuthTokenUtil.AUTHORIZATION_METHOD_PATH;

/**
 * Remote Device service
 */
@RegisterRestClient(configKey = "device-service")
@Path("/api/devices")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeviceService {

    @GET
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    Set<DeviceDTO> getAllDevices(@QueryParam("firstResult") Integer firstResult,
                                 @QueryParam("maxResults") Integer maxResults);

    @POST
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    DeviceDTO create(DeviceDTO device);

    @GET
    @Path("serial/{serialNo}")
    @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
    DeviceDTO getBySerialNo(@PathParam("serialNo") String serialNo);
}
