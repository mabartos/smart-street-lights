package org.smartlights.data.services;

import org.smartlights.data.dto.DeviceDataDTO;

/**
 * Core data business logic
 */
public interface DataService {

    /**
     * Handle data
     *
     * @param data
     * @return state
     */
    boolean handleData(DeviceDataDTO data);

    /**
     * Handle data for device
     *
     * @param deviceID
     * @param data
     * @return state
     */
    boolean handleData(Long deviceID, DeviceDataDTO data);

    /**
     * Handle data from Kafka source
     *
     * @param data
     */
    void handleKafkaData(DeviceDataDTO data);
}
