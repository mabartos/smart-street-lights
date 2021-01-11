package org.smartlights.user.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.smartlights.user.data.AuthUserDTO;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Transactional
@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Auth Resource API", description = "Provide API for authentication")
public interface AuthResource {

    @POST
    @Path("token")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Get Access token", description = "This endpoint returns access token for AuthN/AuthZ")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class))),
            @APIResponse(responseCode = "400", description = "No data provided"),
            @APIResponse(responseCode = "404", description = "User not found"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    String getAccessToken(AuthUserDTO authUserDTO);
}
