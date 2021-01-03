package org.smartlights.city.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="CITIES")
public class CityEntity extends PanacheEntity {
    @Column(unique = true, nullable = false)
    public Long cityId;

    @Column
    public String cityName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityEntity that = (CityEntity) o;
        return Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId);
    }
}
