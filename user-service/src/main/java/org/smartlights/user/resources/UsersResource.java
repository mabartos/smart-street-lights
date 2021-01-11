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
import org.smartlights.user.utils.Constants;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Transactional
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@Tag(name = "Users Resource API", description = "Provide API for all users.")
public interface UsersResource {

    @GET
    @RolesAllowed({UserRole.ADMIN, UserRole.SYS_ADMIN})
    @Operation(summary = "Get all Users", description = "Returns all users")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    Set<UserDTO> getAllUsers(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                             @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults);

    @GET
    @Path("username/{username}")
    @Operation(summary = "Get User by username", description = "Returns user found by username")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserDTO.class))),
            @APIResponse(responseCode = "404", description = "User not found"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    UserDTO getByUsername(@PathParam("username") String username);

    @POST
    @PermitAll
    @Operation(summary = "Create User", description = "Returns created user")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserDTO.class))),
            @APIResponse(responseCode = "400", description = "Wrong attributes"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "409", description = "Conflict")
    })
    UserDTO createUser(@NotNull UserDTO user);

    @PATCH
    @RolesAllowed({UserRole.ADMIN})
    @Operation(summary = "Update user", description = "Returns updated user")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserDTO.class))),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "404", description = "User not found"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    UserDTO updateUser(@NotNull UserDTO user);

    @Path("{id}")
    UserResource forwardToUserResource(@PathParam("id") Long userID, UserSession session);
}
