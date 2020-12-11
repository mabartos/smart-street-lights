package org.smartlights.device.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.DeviceRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DeviceSerializer {

    @Inject
    DeviceRepository deviceRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public DeviceEntity modelToEntity(DeviceDTO deviceDTO) {
        DeviceEntity entity = deviceRepository.getByID(deviceDTO.id);
        if (entity != null) {
            entity.parent = deviceRepository.getByID(deviceDTO.parentID);
            entity.cityID = deviceDTO.cityID;
            entity.streetID = deviceDTO.streetID;
            entity.serialNo = deviceDTO.serialNo;
            entity.type = deviceDTO.type;
            Set<DeviceEntity> neighbors = deviceRepository.getAllFromSetID(deviceDTO.neighborsID).collect(Collectors.toSet());
            entity.neighbors.addAll(neighbors);
            return entity;
        }
        return null;
    }

    public static DeviceDTO entityToModel(DeviceEntity deviceEntity) {
        DeviceDTO dto = objectMapper.convertValue(deviceEntity, new TypeReference<DeviceDTO>() {
        });
        dto.parentID = deviceEntity.parent != null ? deviceEntity.parent.id : -1;
        dto.neighborsID = deviceEntity.neighbors
                .stream()
                .map(f -> f.id)
                .collect(Collectors.toSet());
        return dto;
    }
}
