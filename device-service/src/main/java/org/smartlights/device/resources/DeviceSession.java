package org.smartlights.device.resources;

import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.DeviceDataRepository;
import org.smartlights.device.entity.DeviceRepository;
import org.smartlights.device.entity.NeighborsRepository;
import org.smartlights.device.services.DeviceService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class DeviceSession {

    @Inject
    DeviceService deviceService;

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    DeviceDataRepository deviceDataRepository;

    @Inject
    NeighborsRepository neighborsRepository;

    @Inject
    DeviceSerializer deviceSerializer;

    private Long actualDeviceID;

    public DeviceService getDeviceService() {
        return deviceService;
    }

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public NeighborsRepository getNeighborsRepository() {
        return neighborsRepository;
    }

    public DeviceDataRepository getDeviceDataRepository() {
        return deviceDataRepository;
    }

    public DeviceSerializer getDeviceSerializer() {
        return deviceSerializer;
    }

    public Long getActualDeviceID() {
        return actualDeviceID;
    }

    public DeviceSession setActualDeviceID(Long deviceID) {
        this.actualDeviceID = deviceID;
        return this;
    }
}
