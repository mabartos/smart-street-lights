package org.smartlights.data.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.smartlights.data.utils.DeviceType;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDTO implements Serializable {
    public Long id;
    public String serialNo;
    public Long cityID;
    public Long streetID;
    public DeviceType type;
}
