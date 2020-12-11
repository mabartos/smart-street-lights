package org.smartlights.device.dto;

import org.smartlights.device.utils.DeviceType;

import java.sql.Timestamp;

public class DeviceDataDTO {
    public Timestamp timestamp;
    public Long deviceSerialNo;
    public DeviceType deviceType;
    public Long deviceID;
}
