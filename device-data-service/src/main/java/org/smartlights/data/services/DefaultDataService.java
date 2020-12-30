package org.smartlights.data.services;


import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.entity.repository.DeviceDataRepository;
import org.smartlights.data.utils.DeviceDataProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class DefaultDataService implements DataService {

    private static final Logger logger = Logger.getLogger(DefaultDataService.class.getName());

    @Inject
    DeviceDataRepository dataRepository;

    @Override
    public boolean handleData(DeviceDataDTO data) {
        return handleData(null, data);
    }

    @Override
    public boolean handleData(Long deviceID, DeviceDataDTO data) {
        logger.info("---------DATA----------");
        logger.info("Device: " + Optional.ofNullable(deviceID).orElseGet(() -> data.deviceID));
        data.values.entrySet()
                .stream()
                .filter(entry -> DeviceDataProperty.containsProperty(entry.getKey()))
                .forEach(f -> logger.info("Key: " + f.getKey() + ", Value: " + f.getValue()));
        logger.info("-----------------------");
        return dataRepository.saveData(data);
    }
}
