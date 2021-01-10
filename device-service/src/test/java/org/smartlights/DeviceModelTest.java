package org.smartlights;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.repository.DeviceRepository;
import org.smartlights.device.utils.DeviceType;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class DeviceModelTest {

    @Inject
    DeviceRepository repository;

    @Test
    public void testGetDevices() {
        final long deviceCount = repository.getAll().count();
        final int createDevicesCount = 3;
        assertEquals(createDevices(createDevicesCount).size(), createDevicesCount);
        assertEquals(deviceCount + createDevicesCount, repository.getAll().count());
    }

    @Test
    public void testCreateDevice() {
        final Integer uniqueNumber = 5;
        DeviceEntity device = createDevice(uniqueNumber);
        assertNotNull(device);
        assertEquals("serial" + uniqueNumber, device.serialNo);
        assertEquals(DeviceType.LIGHT, device.type);
        assertEquals(0L, device.streetID);
        assertEquals(0L, device.cityID);
        assertNull(device.parent);
    }

    @Test
    public void testRemoveDevice() {
        final int createDevicesCount = 3;
        long devicesCount = repository.getAll().count();

        Set<DeviceEntity> devices = createDevices(createDevicesCount);
        devicesCount += createDevicesCount;
        assertEquals(devicesCount, repository.getAll().count());

        DeviceEntity device = devices.stream().findFirst().orElse(null);
        assertNotNull(device);
        assertTrue(repository.deleteByID(device.id));
        assertEquals(devicesCount - 1, repository.getAll().count());
    }

    @Test
    public void testUpdateDevice() {
        final int uniqueInt = new Random().nextInt();

        DeviceEntity device = createDevice(uniqueInt);
        assertNotNull(device);

        device.serialNo = "serial" + uniqueInt;
        device.type = DeviceType.PHOTO_RESISTOR;
        device.cityID = 1L;
        device.streetID = 1L;

        DeviceEntity updated = repository.update(device);
        assertNotNull(updated);
        assertCreatedDevice(device, updated);
    }

    private DeviceEntity createDevice() {
        return createDevice(null, null);
    }

    private DeviceEntity createDevice(Integer uniqueNumber) {
        return createDevice(uniqueNumber, null);
    }

    private DeviceEntity createDevice(Integer uniqueNumber, DeviceType type) {
        Random random = new Random();
        final int randomNumber = uniqueNumber != null ? uniqueNumber : random.nextInt();
        DeviceEntity device = new DeviceEntity();
        device.serialNo = "serial" + randomNumber;
        device.type = Optional.ofNullable(type).orElse(DeviceType.LIGHT);
        device.cityID = 0L;
        device.streetID = 0L;
        device.parent = null;
        return repository.create(device);
    }

    private void assertCreatedDevice(DeviceEntity expected, DeviceEntity actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.serialNo, actual.serialNo);
        assertEquals(expected.cityID, actual.cityID);
        assertEquals(expected.streetID, actual.streetID);
        assertEquals(expected.neighbors.size(), actual.neighbors.size());
        assertEquals(expected.parent, actual.parent);
    }

    private Set<DeviceEntity> createDevices(int count) {
        Set<DeviceEntity> devices = new HashSet<>();
        for (int i = 0; i < count; i++) {
            DeviceEntity device = createDevice();
            assertNotNull(device);
            devices.add(device);
        }
        return devices;
    }
}
