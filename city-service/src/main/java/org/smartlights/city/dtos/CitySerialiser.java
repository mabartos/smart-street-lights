package org.smartlights.street.dtos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.street.entities.CityEntity;
import org.smartlights.street.entities.CityRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CitySerialiser {

    @Inject
    CityRepository streetRepository;

    private static ObjectMapper mapper;

    public CitySerialiser() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public CityEntity modelToEntity(CityDTO streetDTO) {
        return mapper.convertValue(streetDTO, CityEntity.class);
    }

    public CityDTO entityToModel(CityEntity streetEntity) {
        return mapper.convertValue(streetEntity, CityDTO.class);
    }
}
