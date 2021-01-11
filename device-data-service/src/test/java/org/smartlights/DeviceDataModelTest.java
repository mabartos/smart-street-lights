package org.smartlights;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.entity.DeviceDataEntity;
import org.smartlights.data.entity.repository.DeviceDataRepository;
import org.smartlights.data.utils.DeviceType;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class DeviceDataModelTest {

    @Inject
    DeviceDataRepository repository;

    private final Long deviceID = 5L;

    @Test
    public void testGetData() {
        final long dataCount = repository.getCountOfDeviceData(deviceID);
        final int createDataCount = 3;
        assertEquals(createMoreData(deviceID, createDataCount).size(), createDataCount);
        assertEquals(dataCount + createDataCount, repository.getCountOfDeviceData(deviceID));
    }

    @Test
    public void testCreateData() {
        final Integer uniqueNumber = 5;
        DeviceDataEntity data = createData(deviceID, DeviceType.LIGHT, uniqueNumber);
        assertNotNull(data);
    }

    private DeviceDataEntity createData(Long deviceID) {
        return createData(deviceID, null, null);
    }

    private DeviceDataEntity createData(Long deviceID, DeviceType deviceType, Integer uniqueNumber) {
        DeviceDataDTO data = new DeviceDataDTO();
        data.serialNo = "serial" + deviceID;
        data.deviceID = deviceID;
        data.timestamp = new Date();
        data.type = Optional.ofNullable(deviceType).orElse(DeviceType.LIGHT);

        Assertions.assertTrue(repository.saveData(data));
        return repository.getAllFromDevice(deviceID)
                .reduce((first, second) -> second)
                .orElse(null);
    }

    private Set<DeviceDataEntity> createMoreData(Long deviceID, int count) {
        Set<DeviceDataEntity> data = new HashSet<>();
        for (int i = 0; i < count; i++) {
            DeviceDataEntity data1 = createData(deviceID);
            assertNotNull(data1);
            data.add(data1);
        }
        return data;
    }

}
