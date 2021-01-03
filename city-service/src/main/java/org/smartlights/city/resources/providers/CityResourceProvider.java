package org.smartlights.city.resources.providers;

import org.smartlights.city.dtos.CityDTO;
import org.smartlights.city.resources.CityResource;
import org.smartlights.city.resources.CitySession;

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
    public CityDTO getByID() {
        return entityToModel(session.getCityRepository().getById(cityId));
    }

    @DELETE
    public boolean removeCity() {
        return session.getCityRepository().deleteCityByID(cityId);
    }
}
