package org.smartlights.device.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.TypedQuery;
import java.util.stream.Stream;

public class EntityUtils {

    public static TypedQuery<DeviceEntity> pagination(TypedQuery<DeviceEntity> query, Integer firstResult, Integer maxResults) {
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query;
    }

    public static <T extends PanacheEntity> Stream<T> pagination(Stream<T> stream, Integer firstResult, Integer maxResults) {
        return stream.skip(firstResult != null ? firstResult : 0).limit(maxResults != null ? maxResults : Integer.MAX_VALUE);
    }
}
