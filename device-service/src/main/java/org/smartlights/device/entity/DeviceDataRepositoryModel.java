package org.smartlights.device.entity;

import java.sql.Timestamp;
import java.util.stream.Stream;

public interface DeviceDataRepositoryModel {

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
