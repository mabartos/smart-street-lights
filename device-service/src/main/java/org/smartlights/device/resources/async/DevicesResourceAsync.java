package org.smartlights.device.resources.async;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.smartlights.device.client.UserRole;
import org.smartlights.device.dto.DeviceDTO;
import org.smartlights.device.utils.Constants;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("devices/async")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Devices Resource Async API", description = "Provide asynchronous API for all devices.")
@Authenticated
public interface DevicesResourceAsync {

    @GET
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN})
    @Operation(summary = "Get all devices")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
    })
    Multi<DeviceDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                            @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("serial/{serialNo}")
    @Operation(summary = "Get device by serial number", description = "Returns device by serial no.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Uni<DeviceDTO> getBySerialNo(@PathParam("serialNo") String serialNo);

    @GET
    @Path("fromStreet/{streetID}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Operation(summary = "Get all devices from street")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Street not found")
    })
    Multi<DeviceDTO> getAllFromStreet(@PathParam(Constants.STREET_ID_PARAM) String streetID,
                                      @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                      @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("fromCity/{cityID}")
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Operation(summary = "Get all devices from city")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "City not found")
    })
    Multi<DeviceDTO> getAllFromCity(@PathParam(Constants.CITY_ID_PARAM) String cityID,
                                    @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                    @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @POST
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Operation(summary = "Create device")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "409", description = "Conflict")
    })
    Uni<DeviceDTO> create(DeviceDTO device);

    @PUT
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Operation(summary = "Update device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "400", description = "Bad request")
    })
    Uni<DeviceDTO> update(DeviceDTO device);
}
