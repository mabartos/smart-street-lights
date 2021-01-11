package org.smartlights.data.services;

import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricID;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.resources.DataSession;
import org.smartlights.data.utils.Constants;
import org.smartlights.data.utils.DeviceDataProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

@ApplicationScoped
public class DefaultDataService implements DataService {

    private static final Logger logger = Logger.getLogger(DefaultDataService.class.getName());
    private static final String REST_SOURCE = "REST source";
    private static final String KAFKA_SOURCE = "Kafka source";

    @Inject
    DataSession session;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry metricRegistry;

    @Override
    @Timed(name = "handleDataSource")
    @Counted(name = "handleDataSourceCount")
    public boolean handleData(DeviceDataDTO data) {
        return handleData(null, data);
    }

    @Override
    @Timed(name = "handleDataSource")
    @Counted(name = "handleDataSourceCount")
    public boolean handleData(Long deviceID, DeviceDataDTO data) {
        return handleDataFromDifferentSources(deviceID, data, REST_SOURCE);
    }

    @Incoming("data")
    @Blocking
    @Transactional
    public void handleKafkaData(DeviceDataDTO data) {
        handleDataFromDifferentSources(null, data, KAFKA_SOURCE);
    }

    /**
     * Helper method for handling data from different sources
     * TODO: With Kafka, data are not saved.
     *
     * @param deviceID
     * @param data
     * @param source
     * @return state
     */
    private boolean handleDataFromDifferentSources(Long deviceID, DeviceDataDTO data, String source) {
        updateMetrics(data);

        logger.info("---------DATA---" + source + "-------");
        final Long finalDeviceID = Optional.ofNullable(deviceID).orElseGet(() -> data.deviceID);
        logger.info("Device: " + finalDeviceID);
        data.values.entrySet()
                .stream()
                .filter(entry -> DeviceDataProperty.containsProperty(entry.getKey()))
                .forEach(f -> logger.info("Key: " + f.getKey() + ", Value: " + f.getValue()));
        logger.info("-----------------------");

        //TODO cannot save data with kafka
        return !source.equals(REST_SOURCE) || session.getDataRepository().saveData(deviceID, data);
    }

    private void updateMetrics(DeviceDataDTO data) {
        Function<String, String> getMetricString = (key) -> Constants.DEVICE_NAME + "-" + data.serialNo + "-" + key;

        BiFunction<String, MetricType, Metadata> metadata = (key, metricType) -> Metadata.builder()
                .withName(getMetricString.apply(key))
                .withDisplayName("Device no. " + data.serialNo)
                .withType(metricType)
                .reusable()
                .build();

        metricRegistry.counter(metadata.apply("dataCount", MetricType.COUNTER)).inc();

        data.values.forEach((key, value) -> {
            if (metricRegistry.getMetricIDs().contains(new MetricID(getMetricString.apply(key)))) {
                metricRegistry.remove(getMetricString.apply(key));
            }

            metricRegistry.register(metadata.apply(key, MetricType.GAUGE), (Gauge<Object>) () -> convert(value));
        });
    }

    private Object convert(Object object) {
        if (object instanceof Boolean) {
            return (Boolean) object ? 1 : 0;
        }
        return object;
    }
}
