package org.smartlights.city.resources;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.smartlights.city.dtos.CityDTO;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface CityResource {

    @GET
    CityDTO getByID();

    @DELETE
    boolean removeCity();

}
