package org.smartlights.device.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.smartlights.device.utils.DeviceType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDataDTO implements Serializable {
    public Timestamp timestamp;
    public Long deviceSerialNo;
    public DeviceType deviceType;
    public Long deviceID;
    public Map<String, Object> values;
}
