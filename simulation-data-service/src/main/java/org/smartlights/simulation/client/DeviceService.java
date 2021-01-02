package org.smartlights.simulation.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.smartlights.simulation.model.DeviceDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@RegisterRestClient(configKey = "device-service")
@Path("/api/devices")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeviceService {

    @GET
    Set<DeviceDTO> getAllDevices(@QueryParam("firstResult") Integer firstResult,
                                 @QueryParam("maxResults") Integer maxResults);

    @POST
    DeviceDTO create(DeviceDTO device);
}
