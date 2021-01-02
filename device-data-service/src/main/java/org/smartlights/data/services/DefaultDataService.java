package org.smartlights.data.services;


import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.resources.DataSession;
import org.smartlights.data.utils.DeviceDataProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class DefaultDataService implements DataService {

    private static final Logger logger = Logger.getLogger(DefaultDataService.class.getName());

    @Inject
    DataSession session;

    @Override
    public boolean handleData(DeviceDataDTO data) {
        return handleData(null, data);
    }

    @Override
    public boolean handleData(Long deviceID, DeviceDataDTO data) {
        logger.info("---------DATA----------");
        final Long finalDeviceID = Optional.ofNullable(deviceID).orElseGet(() -> data.deviceID);
        logger.info("Device: " + finalDeviceID);
        data.values.entrySet()
                .stream()
                .filter(entry -> DeviceDataProperty.containsProperty(entry.getKey()))
                .forEach(f -> logger.info("Key: " + f.getKey() + ", Value: " + f.getValue()));
        logger.info("-----------------------");
        return session.getDataRepository().saveData(deviceID, data);
    }
}
