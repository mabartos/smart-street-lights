package org.smartlights.device.entity;

import java.util.stream.Stream;

public interface NeighborsRepositoryModel {

    Stream<Long> getAllID(Long parentID);

    Stream<DeviceEntity> getAll(Long parentID);

    Stream<Long> getAllID(Long parentID, Integer firstResult, Integer maxResults);

    Stream<DeviceEntity> getAll(Long parentID, Integer firstResult, Integer maxResults);

    DeviceEntity getByID(Long parentID, Long id);

    boolean addNeighbor(Long parentID, Long id);

    boolean removeNeighbor(Long parentID, Long id);

    Integer getNeighborsCount(Long parentID);
}
