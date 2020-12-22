package org.smartlights.simulation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDataDTO implements Serializable {
    public Long id;
    public Date timestamp;
    public String serialNo;
    public Long deviceID;
    public DeviceType type;
    public Map<String, Object> values = new ConcurrentHashMap<>();
}