package org.smartlights.device.entity;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.smartlights.device.entity.repository.DeviceRepository;
import org.smartlights.device.utils.DeviceType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class TestingData {

    @Inject
    DeviceRepository deviceRepository;

    @ConfigProperty(name = "device.testing-data", defaultValue = "false")
    Boolean generateTestingData;

    private static final Logger logger = Logger.getLogger(TestingData.class.getName());

    public void addTestingData(@Observes StartupEvent start) {
        if (!generateTestingData) return;

        final int ENTITY_COUNT = 10;
        logger.info("Adding testing data...");

        for (int i = 0; i < ENTITY_COUNT; i++) {
            DeviceEntity entity = new DeviceEntity();
            entity.serialNo = "serial" + i;
            entity.streetID = i % 3L;
            entity.cityID = i % 2L;
            entity.type = DeviceType.LIGHT;
            entity = deviceRepository.create(entity);
            logger.info("Added device with serialNo: " + entity.serialNo);
        }
    }
}
