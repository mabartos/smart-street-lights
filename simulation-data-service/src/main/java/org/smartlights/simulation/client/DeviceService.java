package org.smartlights.simulation.client;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.smartlights.simulation.model.DeviceDTO;
import org.smartlights.simulation.model.DeviceDataDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@RegisterRestClient(configKey = "device-service")
@Path("/api/devices/async")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeviceService {

    @GET
    Multi<DeviceDTO> getAll();

    @GET
    Set<DeviceDTO> getAllDevices(@QueryParam("firstResult") Integer firstResult, @QueryParam("maxResults") Integer maxResults);

    @POST
    DeviceDTO create(DeviceDTO device);

    @POST
    @Path("{id}/data")
    Response sendData(@PathParam("id") Long id, DeviceDataDTO data);
}
