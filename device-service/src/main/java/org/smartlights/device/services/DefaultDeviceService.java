package org.smartlights.device.services;

import org.smartlights.device.dto.DeviceDataDTO;
import org.smartlights.device.entity.DeviceDataRepository;
import org.smartlights.device.utils.DeviceDataProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class DefaultDeviceService implements DeviceService {

    @Inject
    DeviceDataRepository dataRepository;

    @Override
    public boolean handleData(DeviceDataDTO data) {
        return handleData(null, data);
    }

    @Override
    public boolean handleData(Long deviceID, DeviceDataDTO data) {
        System.out.println("Data:");
        System.out.println("device: " + Optional.ofNullable(deviceID).orElseGet(() -> data.deviceID));
        data.values.entrySet()
                .stream()
                .filter(entry -> DeviceDataProperty.containsProperty(entry.getKey()))
                .forEach(f -> System.out.println("Key: " + f.getKey() + ", Value: " + f.getValue()));
        return dataRepository.saveData(data);
    }
}
