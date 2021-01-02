package org.smartlights.device.resources;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.smartlights.device.client.DeviceDataService;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.repository.DeviceRepository;
import org.smartlights.device.entity.repository.NeighborsRepository;
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
    NeighborsRepository neighborsRepository;

    @Inject
    DeviceSerializer deviceSerializer;

    @Inject
    @RestClient
    DeviceDataService deviceDataService;

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

    public DeviceDataService getDeviceDataService() {
        return deviceDataService;
    }
}
