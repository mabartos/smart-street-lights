package org.smartlights.device.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.smartlights.device.utils.DeviceType;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDataDTO {
    public Timestamp timestamp;
    public Long deviceSerialNo;
    public DeviceType deviceType;
    public Long deviceID;
}
