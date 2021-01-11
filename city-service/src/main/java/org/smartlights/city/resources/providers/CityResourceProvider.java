package org.smartlights.city.resources.providers;

import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.city.client.UserRole;
import org.smartlights.city.dtos.CityDTO;
import org.smartlights.city.resources.CityResource;
import org.smartlights.city.resources.CitySession;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.smartlights.city.dtos.CitySerialiser.entityToModel;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class CityResourceProvider implements CityResource {

    private final CitySession session;
    private final Long cityId;

    public CityResourceProvider(CitySession session) {
        this.session = session;
        this.cityId = session.getCityID();
    }

    @GET
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN, UserRole.MAINTAINER})
    @Timed(name = "getCityByIdTime", description = "A measure of how long it takes to get city by ID.")
    public CityDTO getByID() {
        return entityToModel(session.getCityRepository().getById(cityId));
    }

    @DELETE
    @RolesAllowed({UserRole.SYS_ADMIN, UserRole.ADMIN})
    @Timed(name = "removeCity", description = "A measure of how long it takes to delete city by ID.")
    public boolean removeCity() {
        return session.getCityRepository().deleteCityByID(cityId);
    }
}
