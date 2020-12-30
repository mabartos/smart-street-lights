package org.smartlights.data.dto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.data.entity.DeviceDataEntity;
import org.smartlights.data.utils.DeviceDataProperty;

import java.util.Map;
import java.util.stream.Collectors;

public class DataSerializer {

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static DeviceDataEntity modelToEntity(DeviceDataDTO deviceDataDTO) {
        DeviceDataEntity entity = getObjectMapper().convertValue(deviceDataDTO, DeviceDataEntity.class);
        entity.values = deviceDataDTO.values.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, f -> String.valueOf(f.getValue())));
        return entity;
    }

    public static DeviceDataDTO entityToModel(DeviceDataEntity deviceDataEntity) {
        DeviceDataDTO dto = getObjectMapper().convertValue(deviceDataEntity, DeviceDataDTO.class);
        dto.values = deviceDataEntity.values.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, DeviceDataProperty::convertToTypeByKey));
        return dto;
    }
}
