package org.smartlights.device.entity.repository.model;

import org.smartlights.device.entity.DeviceEntity;

import java.util.Set;
import java.util.stream.Stream;

public interface DeviceRepositoryModel {

    Integer PAGE_COUNT = 25;

    DeviceEntity create(DeviceEntity deviceEntity);

    DeviceEntity getByID(Long id);

    DeviceEntity getWholeByID(Long id);

    DeviceEntity getByIDWithData(Long id);

    DeviceEntity getBySerialNo(String serialNo);

    Stream<DeviceEntity> getAllFromSetID(Set<Long> deviceSet);

    Stream<DeviceEntity> getAllFromSetID(Stream<Long> deviceSet);

    Stream<DeviceEntity> getAll(Integer firstResult, Integer maxResults);

    Stream<DeviceEntity> getAllFromStreet(String streetID, Integer firstResult, Integer maxResults);

    Stream<DeviceEntity> getAllFromCity(String cityID, Integer firstResult, Integer maxResults);

    boolean deleteDevice(DeviceEntity deviceEntity);

    boolean deleteByID(Long id);

    DeviceEntity update(DeviceEntity deviceEntity);
}
