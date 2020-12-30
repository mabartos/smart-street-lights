package org.smartlights.data.resources;

import org.smartlights.data.entity.repository.DeviceDataRepository;
import org.smartlights.data.services.DataService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class DataSession {

    @Inject
    DeviceDataRepository dataRepository;

    @Inject
    DataService dataService;

    private Long actualDeviceID;

    public Long getActualDeviceID() {
        return actualDeviceID;
    }

    public DataSession setActualDevice(Long deviceID) {
        this.actualDeviceID = deviceID;
        return this;
    }

    public DeviceDataRepository getDataRepository() {
        return dataRepository;
    }
    
    public DataService getDataService() {
        return dataService;
    }
}
