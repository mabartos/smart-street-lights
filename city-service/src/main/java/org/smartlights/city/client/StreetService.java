package org.smartlights.city.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.smartlights.city.client.AuthTokenUtil.AUTHORIZATION_FIELD;
import static org.smartlights.city.client.AuthTokenUtil.AUTHORIZATION_METHOD_PATH;

    @RegisterRestClient(configKey = "street-service")
    @Path("/api/street")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public interface StreetService {

        @GET
        @ClientHeaderParam(name = AUTHORIZATION_FIELD, value = AUTHORIZATION_METHOD_PATH, required = false)
        Set<StreetDTO> getAllStreetsFromCity(@QueryParam("cityID") Long cityID,
                                             @QueryParam("firstResult") Integer  firstResult,
                                             @QueryParam("maxResults") Integer maxResults);
    }


