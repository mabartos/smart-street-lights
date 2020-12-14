package org.smartlights.device.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.smartlights.device.utils.DeviceType;

import java.io.Serializable;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDTO implements Serializable {
    public Long id;
    public String serialNo;
    public Long cityID;
    public Long streetID;
    public DeviceType type;
    public Long parentID;
    public Set<Long> neighborsID;
}
