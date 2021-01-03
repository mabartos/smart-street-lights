package org.smartlights.city.resources;

import org.smartlights.city.entities.CityRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class CitySession {

    @Inject
    CityRepository cityRepository;

    private Long cityID;

    public CityRepository getCityRepository() {
        return cityRepository;
    }

    public Long getCityID() {
        return cityID;
    }

    public CitySession setCityID(Long cityID) {
        this.cityID = cityID;
        return this;
    }

}
