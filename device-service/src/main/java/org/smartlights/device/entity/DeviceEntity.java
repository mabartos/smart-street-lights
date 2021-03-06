package org.smartlights.device.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.vertx.core.impl.ConcurrentHashSet;
import org.smartlights.device.utils.DeviceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "DEVICES")
@NamedQueries({
        @NamedQuery(name = "getDeviceByID", query = "select device from DeviceEntity device left join fetch device.parent where device.id=:id"),
        @NamedQuery(name = "getWholeDevice", query = "select device from DeviceEntity device left join fetch device.neighbors left join fetch device.parent where device.id=:id"),
        @NamedQuery(name = "getDevicesFromStreet", query = "select device from DeviceEntity device where device.streetID=:streetID"),
        @NamedQuery(name = "getDevicesFromCity", query = "select device from DeviceEntity device where device.cityID=:cityID"),
        @NamedQuery(name = "getDeviceBySerialNo", query = "select device from DeviceEntity device where device.serialNo=:serialNo")
})
public class DeviceEntity extends PanacheEntity {
    @Column(unique = true, nullable = false)
    public String serialNo;

    @Column
    public Long cityID;

    @Column
    public Long streetID;

    @Column
    public DeviceType type;

    @Version
    private int version;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DeviceEntity.class)
    @JoinColumn
    public DeviceEntity parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    public Set<DeviceEntity> neighbors = new ConcurrentHashSet<>();

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DeviceEntity))
            return false;
        if (this == object)
            return true;

        DeviceEntity deviceEntity = (DeviceEntity) object;
        return id.equals(deviceEntity.id)
                && serialNo.equals(deviceEntity.serialNo)
                && cityID.equals(deviceEntity.cityID)
                && streetID.equals(deviceEntity.streetID)
                && parent.equals(deviceEntity.parent)
                && type.equals(deviceEntity.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNo, cityID, streetID, parent, type);
    }
}
