package org.smartlights.device.entity;

import java.util.stream.Stream;

public interface NeighborsRepositoryModel {

    Stream<Long> getAllID(Long parentID);

    Stream<DeviceEntity> getAll(Long parentID);

    DeviceEntity getByID(Long parentID, Long id);

    boolean addNeighbor(Long parentID, Long id);

    boolean removeNeighbor(Long parentID, Long id);

    Integer getNeighborsCount(Long parentID);
}
