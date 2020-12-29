package org.smartlights.device.entity.repository.model;

import org.smartlights.device.dto.DeviceDataDTO;
import org.smartlights.device.entity.DeviceDataEntity;

import java.sql.Timestamp;
import java.util.stream.Stream;

public interface DeviceDataRepositoryModel {

    Integer DATA_SIZE = 10;

    boolean saveData(Long parentID, DeviceDataDTO data);

    boolean saveData(DeviceDataDTO data);

    boolean saveData(DeviceDataEntity data);

    Stream<DeviceDataEntity> getAllFromParent(Long parentID);

    Stream<DeviceDataEntity> getAllFromParent(Long parentID, Integer firstResult, Integer maxResults);

    Stream<DeviceDataEntity> getAllRecentThan(Long parentID, Timestamp timestamp);

    Stream<DeviceDataEntity> getAllRecentThan(Long parentID, Timestamp timestamp, Integer firstResult, Integer maxResults);

    Stream<DeviceDataEntity> getAllOlderThan(Long parentID, Timestamp timestamp);

    Stream<DeviceDataEntity> getAllOlderThan(Long parentID, Timestamp timestamp, Integer firstResult, Integer maxResults);

    DeviceDataEntity getByID(Long id);

    DeviceDataEntity getByIDWithParent(Long id);

    boolean removeByID(Long id);

    boolean removeOlderThan(Long parentID, Timestamp timestamp);
}
