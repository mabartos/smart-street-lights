package org.smartlights.simulation;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.smartlights.simulation.client.DeviceService;
import org.smartlights.simulation.model.DeviceDTO;
import org.smartlights.simulation.model.DeviceDataDTO;
import org.smartlights.simulation.model.DeviceType;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Path("/simulate")
public class SimulationResource {

    @Inject
    @RestClient
    DeviceService deviceService;

    @GET
    @Path("/create-testing-devices")
    public Set<DeviceDTO> createTestingDevices() {
        final int ENTITY_COUNT = 10;
        Set<DeviceDTO> devices = new HashSet<>();

        for (int i = 0; i < ENTITY_COUNT; i++) {
            DeviceDTO entity = new DeviceDTO();
            entity.serialNo = "serial" + (i + 100);
            entity.streetID = i % 3L;
            entity.cityID = i % 2L;
            entity.type = DeviceType.LIGHT;
            devices.add(deviceService.create(entity));
        }
        return devices;
    }

    @GET
    @Path("/send-data")
    public Response sendData() {
        DeviceDataDTO data = new DeviceDataDTO();
        data.timestamp = new Date();
        data.type = DeviceType.LIGHT;
        data.values.put("intensity", 0);
        data.values.put("ambient", 999);
        data.values.put("detect", true);

        Set<DeviceDTO> devices = deviceService.getAllDevices(0, 10);
        devices.forEach(f -> deviceService.sendData(f.id, data));
        return Response.ok().build();
    }
}