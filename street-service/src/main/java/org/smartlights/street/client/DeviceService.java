package org.smartlights.street.client;


import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.smartlights.street.client.AuthTokenUtil.AUTHORIZATION_FIELD;
import static org.smartlights.street.client.AuthTokenUtil.AUTHORIZATION_METHOD_PATH;

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
    Set<DeviceDTO> getAllDevicesFromStreet( @QueryParam("streetID") Long streetID,
                                            @QueryParam("firstResult") Integer firstResult,
                                            @QueryParam("maxResults") Integer maxResults);


}
