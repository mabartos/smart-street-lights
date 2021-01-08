package org.smartlights.device.resources.async;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.smartlights.device.client.UserRole;
import org.smartlights.device.dto.DeviceDTO;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Device Resource Async API", description = "Provide asynchronous API for specific device.")
@Authenticated
@Transactional
public interface DeviceResourceAsync {

    @GET
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Operation(summary = "Get device by ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Uni<DeviceDTO> getByID();

    @DELETE
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Operation(summary = "Delete device by ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Device not found")
    })
    Response delete();
}