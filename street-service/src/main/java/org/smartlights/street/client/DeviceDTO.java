package org.smartlights.street.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.core.impl.ConcurrentHashSet;
import org.smartlights.street.utils.DeviceType;

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
    @JsonIgnore
    public Set<Long> neighborsID = new ConcurrentHashSet<>();
}
