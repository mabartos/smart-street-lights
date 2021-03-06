package org.smartlights.simulation;

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.smartlights.simulation.client.CityService;
import org.smartlights.simulation.client.DeviceDataService;
import org.smartlights.simulation.client.DeviceService;
import org.smartlights.simulation.model.CityDTO;
import org.smartlights.simulation.model.DeviceDTO;
import org.smartlights.simulation.model.DeviceDataDTO;
import org.smartlights.simulation.model.DeviceType;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static org.smartlights.simulation.client.UserRole.SYS_ADMIN;

@Path("/simulate")
@Transactional
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Simulation Resource API", description = "Provide API for simulation.")
public class SimulationResourceProvider implements SimulationResource {
    private static final Logger log = Logger.getLogger(SimulationResourceProvider.class.getName());
    private final String DEFAULT_SHARED_MAP = "DEFAULT_SHARED_MAP";
    private final String SENDING_MAP_ATTRIBUTE = "DATA_SENDING";

    @Inject
    @Channel("data-simulation")
    Emitter<DeviceDataDTO> dataEmitter;

    @Inject
    @RestClient
    DeviceService deviceService;

    @Inject
    @RestClient
    DeviceDataService deviceDataService;

    @Inject
    @RestClient
    CityService cityService;

    @Inject
    Vertx vertx;

    @GET
    @Path("create-testing-devices")
    @RolesAllowed({SYS_ADMIN})
    @Counted(name = "createTestingDevicesCount", description = "Create testing devices counter.")
    @Timed(name = "createTestingDevicesTime", description = "A measure of how long it takes to create testing devices (different count of devices!!).")
    @Retry
    public Set<DeviceDTO> createTestingDevices(@QueryParam("count") @DefaultValue("10") final Integer count,
                                               @QueryParam("includeCities") @DefaultValue("false") Boolean includeCities) {
        Set<DeviceDTO> devices = new HashSet<>();
        Set<CityDTO> cities = new HashSet<>();
        final Random random = new Random();
        Long neighborsID = null;

        if (includeCities) {
            cities = cityService.getAllCities(0, Integer.MAX_VALUE);
            cities = cities.isEmpty() ? createTestingCities(10) : cities;
        }

        for (int i = 0; i < count; i++) {
            DeviceDTO entity = new DeviceDTO();
            entity.serialNo = "serial" + (i + random.nextInt());
            entity.streetID = i % 3L;

            entity.cityID = cities.isEmpty() ? -1L : cities.stream()
                    .skip(i % cities.size())
                    .findFirst()
                    .map(f -> f.cityId)
                    .orElse(-1L);

            entity.type = DeviceType.values()[i % DeviceType.values().length];
            Set<Long> neighborsSet = new HashSet<>();
            Optional.ofNullable(neighborsID).ifPresent(neighborsSet::add);
            entity.neighborsID = neighborsSet;
            entity = deviceService.create(entity);
            neighborsID = entity.id;
            devices.add(entity);
        }
        return devices;
    }

    @GET
    @Path("create-testing-cities")
    @RolesAllowed({SYS_ADMIN})
    @Counted(name = "createTestingCitiesCount", description = "Create testing cities counter.")
    @Timed(name = "createTestingCitiesTime", description = "A measure of how long it takes to create testing cities (different count of cities!!).")
    @Timeout
    @Retry
    public Set<CityDTO> createTestingCities(@QueryParam("count") @DefaultValue("10") final Integer count) {
        Set<CityDTO> cities = new HashSet<>();
        final Random random = new Random();

        for (int i = 0; i < count; i++) {
            CityDTO city = new CityDTO();
            city.cityName = "city" + i + "-" + random.nextInt();
            cities.add(cityService.create(city));
        }

        return cities;
    }


    @GET
    @Path("send-data-specific/{serial}")
    @RolesAllowed({SYS_ADMIN})
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Timeout
    @Retry
    public Response sendDataSpecific(@PathParam("serial") String serialNo,
                                     @QueryParam("time") @DefaultValue("2000") Long time,
                                     @QueryParam("count") @DefaultValue("5") Integer count) {

        final DeviceDTO device = Optional.ofNullable(deviceService.getBySerialNo(serialNo)).orElseThrow(() -> new NotFoundException("Device not found"));
        sendDataVertex(Collections.singleton(device), time, count);

        return Response.ok("Generating of data...").build();
    }

    @GET
    @Path("send-data")
    @RolesAllowed({SYS_ADMIN})
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Counted(name = "sendDataCount", description = "Send data counter.")
    @Timeout
    @Retry
    public Response sendData(@QueryParam("time") @DefaultValue("2000") Long time,
                             @QueryParam("count") @DefaultValue("5") Integer count,
                             @QueryParam("firstResult") Integer firstResult,
                             @QueryParam("maxResults") Integer maxResults,
                             @QueryParam("single") @DefaultValue("true") Boolean executeAsSingle,
                             @QueryParam("includeCities") @DefaultValue("false") Boolean includeCities) {

        LocalMap<String, Boolean> localMap = vertx.sharedData().getLocalMap(DEFAULT_SHARED_MAP);
        Boolean state = Optional.ofNullable(localMap.get(SENDING_MAP_ATTRIBUTE)).orElse(false);

        if (state && executeAsSingle) return Response.status(Response.Status.BAD_REQUEST)
                .entity("Data can be sent only for one instance")
                .build();

        localMap.put(SENDING_MAP_ATTRIBUTE, true);

        try {
            final Set<DeviceDTO> devices = deviceService.getAllDevices(firstResult, maxResults);
            final Set<DeviceDTO> finalDevices = devices.isEmpty() ? createTestingDevices(30, includeCities) : devices;

            sendDataVertex(finalDevices, time, count, localMap);
        } catch (Exception e) {
            log.warning("Stopped generating of data. " + e.getMessage());
            localMap.put(SENDING_MAP_ATTRIBUTE, false);
        }

        return Response.ok("Generating of data...").build();
    }

    private void sendDataVertex(Set<DeviceDTO> destDevices, Long time, Integer count) {
        sendDataVertex(destDevices, time, count, null);
    }

    private void sendDataVertex(Set<DeviceDTO> destDevices, Long time, Integer count, LocalMap<String, Boolean> localMap) {
        AtomicReference<Integer> dataCount = new AtomicReference<>(0);

        try {
            vertx.periodicStream(time)
                    .handler((t) -> {
                        try {
                            dataCount.set(dataCount.get() + 1);
                            log.info("Generated data set: " + dataCount + ". For " + destDevices.size() + " devices.");
                            destDevices.forEach(this::sendDataChoice);
                            if (dataCount.get() >= count) throw new RuntimeException("Achieved the total count.");
                        } catch (Exception e) {
                            log.warning("Stopped generating of data. " + e.getMessage());
                            if (localMap != null) {
                                localMap.put(SENDING_MAP_ATTRIBUTE, false);
                            }
                        }
                    })
                    .pause()
                    .fetch(count);
        } catch (Exception e) {
            log.warning("Stopped generating of data. " + e.getMessage());
            if (localMap != null) {
                localMap.put(SENDING_MAP_ATTRIBUTE, false);
            }
        }
        log.warning("Generating of data... Count of iterations: " + count);
    }

    /**
     * Approach how to send data.
     *
     * @param deviceDTO
     */
    private void sendDataChoice(DeviceDTO deviceDTO) {
        final DeviceDataDTO data = createTestingData(deviceDTO);
        if (data.deviceID % 2 == 0) {
            log.info("Sent by REST");
            deviceDataService.handleData(deviceDTO.id, data);
        } else {
            log.info("Sent by KAFKA");
            dataEmitter.send(data);
        }
    }

    /**
     * Helper method for data generation
     */
    private DeviceDataDTO createTestingData(DeviceDTO device) {
        final Random random = new Random();
        final byte MAX_INTENSITY = 100;
        final int MAX_AMBIENT = 1000;

        final String INTENSITY_FIELD = "intensity";
        final String AMBIENT_FIELD = "ambient";
        final String DETECT_FIELD = "detect";

        DeviceDataDTO data = new DeviceDataDTO();
        data.deviceID = device.id;
        data.serialNo = device.serialNo;
        data.timestamp = new Date();

        switch (device.type) {
            case LIGHT:
                data.type = device.type;
                data.values.put(INTENSITY_FIELD, getRandomInt(0, MAX_INTENSITY));
                data.values.put(AMBIENT_FIELD, getRandomInt(0, MAX_AMBIENT));
                data.values.put(DETECT_FIELD, random.nextBoolean());
                break;
            case PIR:
                data.type = device.type;
                data.values.put(DETECT_FIELD, random.nextBoolean());
                break;
            case PHOTO_RESISTOR:
                data.type = device.type;
                data.values.put(AMBIENT_FIELD, getRandomInt(0, MAX_AMBIENT));
                break;
        }
        return data;
    }

    private int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.ints(min, max + 1).findFirst().getAsInt();
    }
}