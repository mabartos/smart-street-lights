package org.smartlights.simulation;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.smartlights.simulation.model.DeviceDTO;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static org.smartlights.simulation.client.UserRole.SYS_ADMIN;

@Path("simulate")
@Transactional
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Simulation Resource API", description = "Provide API for simulation.")
public interface SimulationResource {

    /**
     * Create testing devices
     *
     * @param count Count of devices, which will be created
     * @return Set of new devices
     */
    @GET
    @Path("create-testing-devices")
    @RolesAllowed({SYS_ADMIN})
    @Operation(summary = "Create testing devices")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "400", description = "Bad request")
    })
    Set<DeviceDTO> createTestingDevices(@QueryParam("count") @DefaultValue("10") final Integer count);

    /**
     * This resource periodically generates data from devices.
     *
     * @param time            Period of generation
     * @param count           Count of periods
     * @param firstResult     ID of first device for which will be the data generated
     * @param maxResults      Count of devices for for which will be the data generated
     * @param executeAsSingle Allow only one instance to generate data
     * @return
     */
    @GET
    @Path("send-data")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RolesAllowed({SYS_ADMIN})
    @Operation(summary = "Send simulated data")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "400", description = "Bad request")
    })
    Response sendData(@QueryParam("time") @DefaultValue("2000") Long time,
                      @QueryParam("count") @DefaultValue("200") Integer count,
                      @QueryParam("firstResult") Integer firstResult,
                      @QueryParam("maxResults") Integer maxResults,
                      @QueryParam("single") @DefaultValue("true") Boolean executeAsSingle);
}
