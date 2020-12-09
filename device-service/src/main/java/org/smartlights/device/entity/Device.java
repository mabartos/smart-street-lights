package org.smartlights.device.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.smartlights.device.utils.DeviceType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "DEVICES")
@NamedQueries({
        @NamedQuery(name = "getDevicesFromStreet", query = "select device from Device device where device.streetID=:streetID"),
        @NamedQuery(name = "getDevicesFromCity", query = "select device from Device device where device.cityID=:cityID")
})
public class Device extends PanacheEntity {
    @Column(unique = true, nullable = false)
    public String serialNo;

    @Column
    public String cityID;

    @Column
    public String streetID;

    @Column
    public DeviceType type;

    @Version
    private int version;

    @ElementCollection
    @JsonIgnore
    public Set<String> neighborsID;

    public boolean equals(Object object) {
        if (!(object instanceof Device))
            return false;
        if (this == object)
            return true;

        Device device = (Device) object;
        return id.equals(device.id)
                && serialNo.equals(device.serialNo)
                && cityID.equals(device.cityID)
                && streetID.equals(device.streetID)
                && type.equals(device.type);
    }

    public int hashCode() {
        return Objects.hash(id, serialNo, cityID, streetID, type);
    }
}
