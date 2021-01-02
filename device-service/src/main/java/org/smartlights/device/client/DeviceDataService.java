package org.smartlights.device.client;

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

@RegisterRestClient(configKey = "device-data-service")
@Path("/api/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeviceDataService {

    @DELETE
    Response removeAllByID(Set<Long> ids);

    @GET
    @Path("device/{id}/count")
    Long countOfDeviceData(@PathParam("id") Long id);

}
