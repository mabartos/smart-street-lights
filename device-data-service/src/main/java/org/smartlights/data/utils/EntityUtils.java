package org.smartlights.data.utils;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.smartlights.data.entity.DeviceDataEntity;

import javax.persistence.TypedQuery;
import java.util.stream.Stream;

/**
 * Helper class for entity
 */
public class EntityUtils {

    /**
     * Provide pagination on TypedQuery
     *
     * @param query
     * @param firstResult
     * @param maxResults
     * @return paginated query
     */
    public static TypedQuery<DeviceDataEntity> pagination(TypedQuery<DeviceDataEntity> query, Integer firstResult, Integer maxResults) {
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query;
    }

    /**
     * Provide pagination on stream
     *
     * @param stream
     * @param firstResult
     * @param maxResults
     * @param <T>
     * @return paginated stream
     */
    public static <T extends PanacheEntity> Stream<T> pagination(Stream<T> stream, Integer firstResult, Integer maxResults) {
        return stream.skip(firstResult != null ? firstResult : 0).limit(maxResults != null ? maxResults : Integer.MAX_VALUE);
    }
}
