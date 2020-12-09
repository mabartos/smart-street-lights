package org.smartlights.device.entity;

import java.util.stream.Stream;

public interface DeviceRepositoryModel {

    Device create(Device device);

    Device getByID(Long id);

    Device getBySerialNo(String serialNo);

    Stream<Device> getAll(Integer firstResult, Integer maxResults);

    Stream<Device> getAllFromStreet(String streetID, Integer firstResult, Integer maxResults);

    Stream<Device> getAllFromCity(String cityID, Integer firstResult, Integer maxResults);

    boolean deleteDevice(Device device);

    boolean deleteByID(Long id);

    Device update(Device device);

}
