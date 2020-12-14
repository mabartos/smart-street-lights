package org.smartlights.device.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.smartlights.device.utils.DeviceType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "DEVICE_DATA")
@NamedQueries({
        @NamedQuery(name = "removeDeviceDataByIDs", query = "delete from DeviceDataEntity device where device.id in :ids"),
        @NamedQuery(name = "getDeviceDataWithParent", query = "select device from DeviceDataEntity device join fetch device.device where device.id =:id")})
public class DeviceDataEntity extends PanacheEntity {

    @Column
    public Timestamp timestamp;

    @Column
    public Long deviceSerialNo;

    @Column
    public DeviceType deviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public DeviceEntity device;

    @Column(name = "VALUE")
    @MapKeyColumn(name = "KEY")
    @ElementCollection
    public Map<String, Object> values;

    public boolean equals(Object object) {
        if (!(object instanceof DeviceDataEntity))
            return false;
        if (this == object)
            return true;

        DeviceDataEntity deviceData = (DeviceDataEntity) object;
        return id.equals(deviceData.id)
                && timestamp.equals(deviceData.timestamp)
                && deviceType.equals(deviceData.deviceType)
                && deviceSerialNo.equals(deviceData.deviceSerialNo)
                && device.equals(deviceData.device)
                && values.equals(deviceData.values);
    }

    public int hashCode() {
        return Objects.hash(id, timestamp, deviceType, deviceSerialNo, device);
    }
}
