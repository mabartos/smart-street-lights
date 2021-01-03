package org.smartlights.street.resources;

import org.smartlights.street.dtos.StreetDTO;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface StreetResource {

    @GET
    StreetDTO getByID();

    @DELETE
    boolean removeStreet();
}
