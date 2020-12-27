package org.smartlights.street.entities;

import org.smartlights.street.utils.StreetCategory;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="STREETS")
@NamedQueries({
        @NamedQuery(name = "getStreetsFromCity", query = "select street from StreetEntity street where street.cityId=:cityId"),
        @NamedQuery(name = "getStreetsByCategory", query = "select street from StreetEntity street where street.streetCategory=:streetCategory"),
})
public class StreetEntity {
    @Column(unique = true, nullable = false)
    public Long streetId;

    @Column
    public String streetName;

    @Column
    public Long cityId;

    @Column
    public StreetCategory streetCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetEntity that = (StreetEntity) o;
        return Objects.equals(streetId, that.streetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetId);
    }
}
