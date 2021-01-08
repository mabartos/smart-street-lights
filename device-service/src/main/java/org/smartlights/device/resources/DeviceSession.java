package org.smartlights.device.resources;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.smartlights.device.client.DeviceDataService;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.repository.DeviceRepository;
import org.smartlights.device.entity.repository.NeighborsRepository;
import org.smartlights.device.services.DeviceService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Handle device session within request
 */
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

    /**
     * Get Device core business logic
     *
     * @return service
     */
    public DeviceService getDeviceService() {
        return deviceService;
    }

    /**
     * Get connection to the Device DB
     *
     * @return repository
     */
    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    /**
     * Get connection to the Neighbors DB
     *
     * @return repository
     */
    public NeighborsRepository getNeighborsRepository() {
        return neighborsRepository;
    }

    /**
     * Get serializer for devices
     *
     * @return serializer
     */
    public DeviceSerializer getDeviceSerializer() {
        return deviceSerializer;
    }

    /**
     * Get actual device within context
     *
     * @return id
     */
    public Long getActualDeviceID() {
        return actualDeviceID;
    }

    /**
     * Set actual device ID
     *
     * @param deviceID
     * @return session
     */
    public DeviceSession setActualDeviceID(Long deviceID) {
        this.actualDeviceID = deviceID;
        return this;
    }

    /**
     * Get remote device data service
     *
     * @return remote service
     */
    public DeviceDataService getDeviceDataService() {
        return deviceDataService;
    }
}
