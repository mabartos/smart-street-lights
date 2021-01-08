package org.smartlights.data.resources;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.smartlights.data.client.DeviceService;
import org.smartlights.data.entity.repository.DeviceDataRepository;
import org.smartlights.data.services.DataService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Handle device data session within request
 */
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

    /**
     * Get actual device ID within context
     */
    public Long getActualDeviceID() {
        return actualDeviceID;
    }

    /**
     * Set actual device
     *
     * @param deviceID
     * @return session
     */
    public DataSession setActualDevice(Long deviceID) {
        this.actualDeviceID = deviceID;
        return this;
    }

    /**
     * Get provided token
     */
    public JsonWebToken getToken() {
        return token;
    }

    /**
     * Get connection to the data DB
     *
     * @return repository
     */
    public DeviceDataRepository getDataRepository() {
        return dataRepository;
    }

    /**
     * Get business logic for data
     *
     * @return service
     */
    public DataService getDataService() {
        return dataService;
    }

    /**
     * Get remote device service
     *
     * @return remote service
     */
    public DeviceService getDeviceService() {
        return deviceService;
    }
}
