package org.smartlights.user.resources;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.smartlights.user.data.UserDTO;
import org.smartlights.user.data.UserRole;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Transactional
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@Tag(name = "User Resource API", description = "Provide API for specific user.")
public interface UserResource {

    @GET
    @Operation(summary = "Get User by ID", description = "Returns user found by ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserDTO.class))),
            @APIResponse(responseCode = "404", description = "User not found"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    UserDTO getUserByID();

    @DELETE
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Operation(summary = "Remove user", description = "Remove User by ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = Boolean.class))),
            @APIResponse(responseCode = "404", description = "User not found"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    boolean removeUser();
}
