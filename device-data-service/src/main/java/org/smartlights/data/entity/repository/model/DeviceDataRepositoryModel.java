package org.smartlights.data.entity.repository.model;


import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.entity.DeviceDataEntity;

import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Define Device Data DB abilities
 */
public interface DeviceDataRepositoryModel {

    /**
     * Default count of results return by bulk operations
     */
    Integer DATA_SIZE = 100;

    /**
     * Save data to DB
     *
     * @param deviceID
     * @param data
     * @return state
     */
    boolean saveData(Long deviceID, DeviceDataDTO data);

    /**
     * Save data to DB
     *
     * @param data
     * @return state
     */
    boolean saveData(DeviceDataDTO data);

    /**
     * Save data to DB
     *
     * @param data
     * @return state
     */
    boolean saveData(DeviceDataEntity data);

    /**
     * Get all data from device
     *
     * @param deviceID
     * @return data
     */
    Stream<DeviceDataEntity> getAllFromDevice(Long deviceID);

    /**
     * Get all data from device with pagination
     *
     * @param deviceID
     * @param firstResult index of the first required device
     * @param maxResults  count of results from the firstResult
     * @return data
     */
    Stream<DeviceDataEntity> getAllFromDevice(Long deviceID, Integer firstResult, Integer maxResults);

    /**
     * Get data newer than timestamp
     *
     * @param deviceID
     * @param date
     * @return data
     */
    Stream<DeviceDataEntity> getAllRecentThan(Long deviceID, Date date);

    /**
     * Get data newer than timestamp with pagination
     *
     * @param deviceID
     * @param timestamp
     * @param firstResult index of the first required device
     * @param maxResults  count of results from the firstResult
     * @return data
     */
    Stream<DeviceDataEntity> getAllRecentThan(Long deviceID, Date date, Integer firstResult, Integer maxResults);

    /**
     * Get data older than timestamp with pagination
     *
     * @param deviceID
     * @param timestamp
     * @return data
     */
    Stream<DeviceDataEntity> getAllOlderThan(Long deviceID, Date date);

    /**
     * Get data older than timestamp with pagination
     *
     * @param deviceID
     * @param timestamp
     * @param firstResult index of the first required device
     * @param maxResults  count of results from the firstResult
     * @return data
     */
    Stream<DeviceDataEntity> getAllOlderThan(Long deviceID, Date date, Integer firstResult, Integer maxResults);

    /**
     * Get data by ID
     *
     * @param id
     * @return data
     */
    DeviceDataEntity getByID(Long id);

    /**
     * Remove data by ID
     *
     * @param id
     * @return state
     */
    boolean removeByID(Long id);

    /**
     * Remove data by set of IDs
     *
     * @param ids set of IDs
     * @return state
     */
    boolean removeByIDs(Set<Long> ids);

    /**
     * Remove data older than timestamp
     *
     * @param deviceID
     * @param timestamp
     * @return state
     */
    boolean removeOlderThan(Long deviceID, Date date);

    /**
     * Get count of data for device
     *
     * @param deviceID
     * @return count
     */
    Long getCountOfDeviceData(Long deviceID);
}
