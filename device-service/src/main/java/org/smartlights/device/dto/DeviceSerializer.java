package org.smartlights.device.dto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.device.entity.DeviceDataEntity;
import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.repository.DeviceDataRepository;
import org.smartlights.device.entity.repository.DeviceRepository;
import org.smartlights.device.entity.repository.NeighborsRepository;
import org.smartlights.device.utils.DeviceDataProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class DeviceSerializer {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    DeviceDataRepository deviceDataRepository;

    @Inject
    NeighborsRepository neighborsRepository;

    private final ObjectMapper mapper;

    public DeviceSerializer() {
        this.mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public DeviceEntity modelToEntity(DeviceDTO deviceDTO) {
        DeviceEntity entity = mapper.convertValue(deviceDTO, DeviceEntity.class);
        entity.parent = deviceDTO.parentID != null && deviceDTO.parentID != -1 ? deviceRepository.getByID(deviceDTO.parentID) : null;
        entity.neighbors = Optional.ofNullable(neighborsRepository.getAll(deviceDTO.id))
                .map(f -> f.collect(Collectors.toSet()))
                .orElseGet(Collections::emptySet);
        return entity;
    }

    public DeviceDTO entityToModel(DeviceEntity deviceEntity) {
        DeviceDTO dto = mapper.convertValue(deviceEntity, DeviceDTO.class);
        dto.parentID = Optional.ofNullable(deviceEntity.parent).map(f -> f.id).orElse(-1L);
        return dto;
    }

    public DeviceDataEntity modelToEntity(DeviceDataDTO deviceDataDTO) {
        DeviceDataEntity entity = mapper.convertValue(deviceDataDTO, DeviceDataEntity.class);
        entity.device = Optional.ofNullable(deviceRepository.getByID(deviceDataDTO.id)).orElse(null);
        entity.values = deviceDataDTO.values.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, f -> String.valueOf(f.getValue())));
        return entity;
    }

    public DeviceDataDTO entityToModel(DeviceDataEntity deviceDataEntity) {
        DeviceDataDTO dto = mapper.convertValue(deviceDataEntity, DeviceDataDTO.class);
        dto.deviceID = Optional.ofNullable(deviceDataRepository.getByIDWithParent(deviceDataEntity.device.id))
                .map(f -> f.id)
                .orElse(-1L);
        dto.values = deviceDataEntity.values.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, DeviceDataProperty::convertToTypeByKey));
        return dto;
    }
}
