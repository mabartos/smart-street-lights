package org.smartlights.device.entity.repository.model;

import org.smartlights.device.entity.DeviceEntity;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Define Device DB abilities
 */
public interface DeviceRepositoryModel {

    /**
     * Default count of results return by bulk operations
     */
    Integer PAGE_COUNT = 25;

    /**
     * Create device
     *
     * @param deviceEntity device representation
     * @return created device
     */
    DeviceEntity create(DeviceEntity deviceEntity);

    /**
     * Get device by ID
     *
     * @param id
     * @return device representation
     */
    DeviceEntity getByID(Long id);

    /**
     * Get device with neighbours and parent
     *
     * @param id
     * @return device representation
     */
    DeviceEntity getWholeByID(Long id);

    /**
     * Get device by serial number
     *
     * @param serialNo serial number
     * @return device representation
     */
    DeviceEntity getBySerialNo(String serialNo);

    /**
     * Get all devices from Set of IDs
     *
     * @param deviceSet set of deviceID
     * @return stream of devices
     */
    Stream<DeviceEntity> getAllFromSetID(Set<Long> deviceSet);

    /**
     * Get all devices from Stream of IDs
     *
     * @param deviceSet stream of deviceID
     * @return stream of devices
     */
    Stream<DeviceEntity> getAllFromSetID(Stream<Long> deviceSet);

    /**
     * Get all Devices
     *
     * @param firstResult index of the first required device
     * @param maxResults  count of results from the firstResult
     * @return stream of devices
     */
    Stream<DeviceEntity> getAll(Integer firstResult, Integer maxResults);

    /**
     * Get all Devices
     *
     * @return stream of devices
     */
    Stream<DeviceEntity> getAll();

    /**
     * Get all Devices by street ID
     *
     * @param firstResult index of the first required user
     * @param maxResults  count of results from the firstResult
     * @return stream of devices
     */
    Stream<DeviceEntity> getAllFromStreet(String streetID, Integer firstResult, Integer maxResults);

    /**
     * Get all Devices by city ID
     *
     * @param firstResult index of the first required user
     * @param maxResults  count of results from the firstResult
     * @return stream of devices
     */
    Stream<DeviceEntity> getAllFromCity(String cityID, Integer firstResult, Integer maxResults);

    /**
     * Delete device
     *
     * @param deviceEntity
     * @return state
     */
    boolean deleteDevice(DeviceEntity deviceEntity);

    /**
     * Delete device by ID
     *
     * @param id
     * @return true if success, otherwise false
     */
    boolean deleteByID(Long id);

    /**
     * Update device
     *
     * @param deviceEntity
     * @return device representation
     */
    DeviceEntity update(DeviceEntity deviceEntity);
}
