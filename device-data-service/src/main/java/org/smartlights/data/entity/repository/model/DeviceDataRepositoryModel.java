package org.smartlights.data.entity.repository.model;


import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.entity.DeviceDataEntity;

import java.sql.Timestamp;
import java.util.stream.Stream;

public interface DeviceDataRepositoryModel {

    Integer DATA_SIZE = 10;

    boolean saveData(Long deviceID, DeviceDataDTO data);

    boolean saveData(DeviceDataDTO data);

    boolean saveData(DeviceDataEntity data);

    Stream<DeviceDataEntity> getAllFromDevice(Long deviceID);

    Stream<DeviceDataEntity> getAllFromDevice(Long deviceID, Integer firstResult, Integer maxResults);

    Stream<DeviceDataEntity> getAllRecentThan(Long deviceID, Timestamp timestamp);

    Stream<DeviceDataEntity> getAllRecentThan(Long deviceID, Timestamp timestamp, Integer firstResult, Integer maxResults);

    Stream<DeviceDataEntity> getAllOlderThan(Long deviceID, Timestamp timestamp);

    Stream<DeviceDataEntity> getAllOlderThan(Long deviceID, Timestamp timestamp, Integer firstResult, Integer maxResults);

    DeviceDataEntity getByID(Long id);
    
    boolean removeByID(Long id);

    boolean removeOlderThan(Long deviceID, Timestamp timestamp);
}
