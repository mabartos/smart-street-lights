package org.smartlights.street.resources;

import org.smartlights.street.entities.StreetRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class StreetSession {

    @Inject
    StreetRepository streetRepository;

    private Long streetID;

    public StreetRepository getStreetRepository() {
        return streetRepository;
    }

    public Long getStreetID() {
        return streetID;
    }

    public StreetSession setStreetID(Long streetID) {
        this.streetID = streetID;
        return this;
    }
}
