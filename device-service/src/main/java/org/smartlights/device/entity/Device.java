package org.smartlights.device.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.smartlights.device.utils.DeviceType;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "DEVICES")
public class Device extends PanacheEntity {
    public String serialNo;
    public String cityID;
    public String streetID;
    public DeviceType type;

    @ElementCollection
    public Set<String> neighbors;
}
