package org.smartlights.data.resources;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.smartlights.data.client.DeviceService;
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

    @Inject
    JsonWebToken token;

    @Inject
    @RestClient
    DeviceService deviceService;

    private Long actualDeviceID;

    public Long getActualDeviceID() {
        return actualDeviceID;
    }

    public DataSession setActualDevice(Long deviceID) {
        this.actualDeviceID = deviceID;
        return this;
    }

    public JsonWebToken getToken() {
        return token;
    }

    public DeviceDataRepository getDataRepository() {
        return dataRepository;
    }
    
    public DataService getDataService() {
        return dataService;
    }

    public DeviceService getDeviceService() {
        return deviceService;
    }
}
