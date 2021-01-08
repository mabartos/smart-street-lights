package org.smartlights.device.entity;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.smartlights.device.entity.repository.DeviceRepository;
import org.smartlights.device.utils.DeviceType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Helper class for testing DB
 */
@ApplicationScoped
public class TestingData {

    @Inject
    DeviceRepository deviceRepository;

    @ConfigProperty(name = "device.testing-data", defaultValue = "false")
    Boolean generateTestingData;

    @ConfigProperty(name = "device.testing-data.count", defaultValue = "10")
    Integer entityCount;

    private static final Logger logger = Logger.getLogger(TestingData.class.getName());

    /**
     * Add testing data to database on Startup
     *
     * @param start
     */
    public void addTestingData(@Observes StartupEvent start) {
        if (!generateTestingData) return;

        logger.info("Adding testing data...");

        for (int i = 0; i < entityCount; i++) {
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
