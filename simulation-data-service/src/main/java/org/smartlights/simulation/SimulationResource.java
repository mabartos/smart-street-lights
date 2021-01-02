package org.smartlights.simulation;

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.smartlights.simulation.client.DeviceDataService;
import org.smartlights.simulation.client.DeviceService;
import org.smartlights.simulation.model.DeviceDTO;
import org.smartlights.simulation.model.DeviceDataDTO;
import org.smartlights.simulation.model.DeviceType;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Path("/simulate")
@Transactional
public class SimulationResource {
    private static final Logger log = Logger.getLogger(SimulationResource.class.getName());
    private final String DEFAULT_SHARED_MAP = "DEFAULT_SHARED_MAP";
    private final String SENDING_MAP_ATTRIBUTE = "DATA_SENDING";

    @Inject
    @RestClient
    DeviceService deviceService;

    @Inject
    @RestClient
    DeviceDataService deviceDataService;

    @Inject
    Vertx vertx;

    /**
     * Create testing devices
     *
     * @param count Count of devices, which will be created
     * @return Set of new devices
     */
    @GET
    @Path("/create-testing-devices")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<DeviceDTO> createTestingDevices(@QueryParam("count") @DefaultValue("10") final Integer count) {
        Set<DeviceDTO> devices = new HashSet<>();
        final Random random = new Random();
        Long neighborsID = null;

        for (int i = 0; i < count; i++) {
            DeviceDTO entity = new DeviceDTO();
            entity.serialNo = "serial" + (i + random.nextInt());
            entity.streetID = i % 3L;
            entity.cityID = i % 2L;
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

    /**
     * This resource periodically generates data from devices.
     *
     * @param time            Period of generation
     * @param count           Count of periods
     * @param firstResult     ID of first device for which will be the data generated
     * @param maxResults      Count of devices for for which will be the data generated
     * @param executeAsSingle Allow only one instance to generate data
     * @return
     */
    @GET
    @Path("/send-data")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Response sendData(@QueryParam("time") @DefaultValue("2000") Long time,
                             @QueryParam("count") @DefaultValue("200") Integer count,
                             @QueryParam("firstResult") Integer firstResult,
                             @QueryParam("maxResults") Integer maxResults,
                             @QueryParam("single") @DefaultValue("true") Boolean executeAsSingle) {

        LocalMap<String, Boolean> localMap = vertx.sharedData().getLocalMap(DEFAULT_SHARED_MAP);
        Boolean state = Optional.ofNullable(localMap.get(SENDING_MAP_ATTRIBUTE)).orElse(false);

        if (state && executeAsSingle) return Response.status(Response.Status.BAD_REQUEST)
                .entity("Data can be sent only for one instance")
                .build();

        localMap.put(SENDING_MAP_ATTRIBUTE, true);

        try {
            Set<DeviceDTO> devices = deviceService.getAllDevices(firstResult, maxResults);
            final Set<DeviceDTO> finalDevices = devices.isEmpty() ? createTestingDevices(30) : devices;
            AtomicReference<Integer> dataCount = new AtomicReference<>(0);

            vertx.periodicStream(time)
                    .handler((t) -> {
                        try {
                            dataCount.set(dataCount.get() + 1);
                            log.info("Generated data set: " + dataCount);
                            finalDevices.forEach(f -> deviceDataService.handleData(f.id, createTestingData(f.type)));
                            if (dataCount.get() >= count) throw new RuntimeException("Achieved the total count.");
                        } catch (Exception e) {
                            log.warning("Stopped generating of data. " + e.getMessage());
                            localMap.put(SENDING_MAP_ATTRIBUTE, false);
                        }
                    })
                    .pause()
                    .fetch(count);
        } catch (Exception e) {
            log.warning("Stopped generating of data. " + e.getMessage());
            localMap.put(SENDING_MAP_ATTRIBUTE, false);
        }

        log.warning("Generating of data... Count of iterations: " + count);
        return Response.ok("Generating of data...").build();
    }

    /**
     * Helper method for data generation
     *
     * @param type Type of Device
     */
    private DeviceDataDTO createTestingData(DeviceType type) {
        final Random random = new Random();
        final byte MAX_INTENSITY = 100;
        final int MAX_AMBIENT = 1000;

        final String INTENSITY_FIELD = "intensity";
        final String AMBIENT_FIELD = "ambient";
        final String DETECT_FIELD = "detect";

        DeviceDataDTO data = new DeviceDataDTO();
        data.timestamp = new Date();

        switch (type) {
            case LIGHT:
                data.type = type;
                data.values.put(INTENSITY_FIELD, getRandomInt(0, MAX_INTENSITY));
                data.values.put(AMBIENT_FIELD, getRandomInt(0, MAX_AMBIENT));
                data.values.put(DETECT_FIELD, random.nextBoolean());
                break;
            case PIR:
                data.type = type;
                data.values.put(DETECT_FIELD, random.nextBoolean());
                break;
            case PHOTO_RESISTOR:
                data.type = type;
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