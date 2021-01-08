package org.smartlights.data.resources;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.utils.Constants;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Set;

import static org.smartlights.data.client.UserRole.ADMIN;
import static org.smartlights.data.client.UserRole.DEVICE;
import static org.smartlights.data.client.UserRole.MAINTAINER;
import static org.smartlights.data.client.UserRole.SYS_ADMIN;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Authenticated
@Tag(name = "Data Device Resource API", description = "Provide API for all device data.")
public interface DataDeviceResource {

    @POST
    @RolesAllowed({DEVICE})
    @Operation(summary = "Handle data")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "400", description = "Bad request")
    })
    Response handleData(DeviceDataDTO data);

    @GET
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Operation(summary = "Count of device data")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = Long.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Long countOfDeviceData();

    @GET
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Operation(summary = "Get all data from Device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDataDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Set<DeviceDataDTO> getAllFromDevice(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                        @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("recent/{timestamp}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Operation(summary = "Get all data recent than timestamp for device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDataDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Set<DeviceDataDTO> getAllRecentThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                        @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                        @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("recent/{timestamp}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Operation(summary = "Get all data older than timestamp for device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDataDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Set<DeviceDataDTO> getAllOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp,
                                       @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                       @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @DELETE
    @Path("{timestamp}")
    @RolesAllowed({SYS_ADMIN, ADMIN})
    @Operation(summary = "Remove data older than timestamp for device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Response removeOlderThan(@PathParam(Constants.TIMESTAMP) Timestamp timestamp);
}
