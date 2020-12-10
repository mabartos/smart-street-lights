package org.smartlights.device.entity;

import java.util.stream.Stream;

public interface NeighborsRepositoryModel {

    Stream<Long> getAllID(Long parentID);

    Stream<Device> getAll(Long parentID);

    Device getByID(Long parentID, Long id);

    boolean addNeighbor(Long parentID, Long id);

    boolean removeNeighbor(Long parentID, Long id);

    Integer getNeighborsCount(Long parentID);
}
