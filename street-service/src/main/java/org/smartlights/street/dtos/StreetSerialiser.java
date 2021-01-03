package org.smartlights.street.dtos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.street.entities.StreetEntity;
import org.smartlights.street.entities.StreetRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class StreetSerialiser {

    @Inject
    StreetRepository streetRepository;

    private static ObjectMapper mapper;

    public StreetSerialiser() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static StreetEntity modelToEntity(StreetDTO streetDTO) {
        return mapper.convertValue(streetDTO, StreetEntity.class);
    }

    public static StreetDTO entityToModel(StreetEntity streetEntity) {
        return mapper.convertValue(streetEntity, StreetDTO.class);
    }
}
