package org.smartlights.data.dto;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Kafka deserializer for data
 */
public class KafkaDeserializer extends ObjectMapperDeserializer<DeviceDataDTO> {
    public KafkaDeserializer() {
        super(DeviceDataDTO.class);
    }
}
