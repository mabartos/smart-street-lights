package org.smartlights.device.resources;

import org.smartlights.device.services.DeviceService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class DeviceSession {

    @Inject
    DeviceService deviceService;

    private Long actualDeviceID;

    public DeviceService getDeviceService() {
        return deviceService;
    }

    public Long getActualDeviceID() {
        return actualDeviceID;
    }

    public DeviceSession setActualDeviceID(Long deviceID) {
        this.actualDeviceID = deviceID;
        return this;
    }
}
