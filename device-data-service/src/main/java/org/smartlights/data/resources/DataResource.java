package org.smartlights.data.resources;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.smartlights.data.dto.DeviceDataDTO;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static org.smartlights.data.client.UserRole.ADMIN;
import static org.smartlights.data.client.UserRole.DEVICE;
import static org.smartlights.data.client.UserRole.MAINTAINER;
import static org.smartlights.data.client.UserRole.SYS_ADMIN;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("data")
@Transactional
@Authenticated
@Tag(name = "Data Resource API", description = "Provide API for all data.")
public interface DataResource {

    @GET
    @Path("test")
    @RolesAllowed(ADMIN)
    @Operation(summary = "Testing endpoint")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
    })
    String test();

    @GET
    @Path("{id}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Operation(summary = "Get data by ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DeviceDataDTO.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Data not found")
    })
    DeviceDataDTO getByID(@PathParam("id") Long id);

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

    @DELETE
    @Path("{id}")
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Operation(summary = "Remove data by ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Data not found")
    })
    Response removeByID(@PathParam("id") Long id);

    @DELETE
    @RolesAllowed({SYS_ADMIN, ADMIN, MAINTAINER})
    @Operation(summary = "Remove all data by ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Data not found")
    })
    Response removeAllByID(Set<Long> ids);

    @Path("device/{id}")
    DataDeviceResource forwardToDeviceData(@PathParam("id") Long deviceID);
}
