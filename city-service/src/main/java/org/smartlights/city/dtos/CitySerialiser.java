package org.smartlights.city.dtos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.city.entities.CityEntity;
import org.smartlights.city.entities.CityRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CitySerialiser {

    @Inject
    CityRepository cityRepository;

    private static ObjectMapper mapper;

    public CitySerialiser() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static CityEntity modelToEntity(CityDTO cityDTO) {
        return mapper.convertValue(cityDTO, CityEntity.class);
    }

    public static CityDTO entityToModel(CityEntity cityEntity) {
        return mapper.convertValue(cityEntity, CityDTO.class);
    }
}
