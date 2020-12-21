package org.smartlights.device.entity;

import io.quarkus.runtime.StartupEvent;
import org.smartlights.device.entity.repository.DeviceDataRepository;
import org.smartlights.device.entity.repository.DeviceRepository;
import org.smartlights.device.utils.DeviceDataProperty;
import org.smartlights.device.utils.DeviceType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class TestingData {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    DeviceDataRepository deviceDataRepository;

    private static final Logger logger = Logger.getLogger(TestingData.class.getName());

    public void addTestingData(@Observes StartupEvent start) {
        final int ENTITY_COUNT = 10;
        final int DATA_ENTITY_COUNT = 5;
        logger.info("Adding testing data...");

        for (int i = 0; i < ENTITY_COUNT; i++) {
            DeviceEntity entity = new DeviceEntity();
            entity.serialNo = "serial" + i;
            entity.streetID = i % 3L;
            entity.cityID = i % 2L;
            entity.type = DeviceType.LIGHT;
            entity = deviceRepository.create(entity);
            logger.info("Added device with serialNo: " + entity.serialNo);

            for (int j = 0; j < DATA_ENTITY_COUNT; j++) {
                DeviceDataEntity data = new DeviceDataEntity();
                //data.timestamp = new Timestamp(new Date().getTime());
                data.device = entity;
                data.type = DeviceType.LIGHT;
                data.serialNo = entity.serialNo;

                Map<String, String> values = new HashMap<>();
                values.put(DeviceDataProperty.INTENSITY.getKey(), "50");
                values.put(DeviceDataProperty.AMBIENT.getKey(), "550");
                values.put(DeviceDataProperty.DETECT.getKey(), "true");

                data.values = values;

                deviceDataRepository.saveData(data);
            }
        }
    }
}
