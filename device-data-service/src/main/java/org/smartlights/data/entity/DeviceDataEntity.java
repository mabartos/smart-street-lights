package org.smartlights.data.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.smartlights.data.utils.DeviceType;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "DEVICE_DATA")
@NamedQueries({
        @NamedQuery(name = "removeDeviceDataByIDs", query = "delete from DeviceDataEntity device where device.id in :ids"),
        @NamedQuery(name = "getAllByDeviceID", query = "select data from DeviceDataEntity data where data.deviceID=:deviceID"),
        @NamedQuery(name = "getCountOfDeviceData", query = "select count(distinct data) from DeviceDataEntity data where data.deviceID=:deviceID")
})
public class DeviceDataEntity extends PanacheEntity {

    @Column
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date timestamp;

    @Column
    public String serialNo;

    @Column
    public DeviceType type;

    @Column
    public Long deviceID;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "device_data_values",
            joinColumns = {@JoinColumn(name = "device_data_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "NAME")
    @Column(name = "VALUE")
    public Map<String, String> values;

    @Version
    private int version;

    public boolean equals(Object object) {
        if (!(object instanceof DeviceDataEntity))
            return false;
        if (this == object)
            return true;

        DeviceDataEntity deviceData = (DeviceDataEntity) object;
        return id.equals(deviceData.id)
                && timestamp.equals(deviceData.timestamp)
                && type.equals(deviceData.type)
                && serialNo.equals(deviceData.serialNo)
                && deviceID.equals(deviceData.deviceID)
                && values.equals(deviceData.values);
    }

    public int hashCode() {
        return Objects.hash(id, timestamp, type, serialNo, deviceID);
    }
}
