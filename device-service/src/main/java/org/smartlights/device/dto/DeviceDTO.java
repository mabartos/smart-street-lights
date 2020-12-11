package org.smartlights.device.dto;


import org.smartlights.device.utils.DeviceType;

import java.util.Set;

public class DeviceDTO {
    public Long id;
    public String serialNo;
    public Long cityID;
    public Long streetID;
    public DeviceType type;
    public Long parentID;
    public Set<Long> neighborsID;
}
