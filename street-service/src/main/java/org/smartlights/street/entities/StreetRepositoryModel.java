package org.smartlights.street.entities;

import org.smartlights.street.utils.StreetCategory;

import java.util.stream.Stream;

public interface StreetRepositoryModel {

    StreetEntity create(StreetEntity streetEntity);

    StreetEntity getById(Long id);

    StreetEntity getByName(String name);

    Stream<StreetEntity> getAll(Integer firstResult, Integer maxResults);

    Stream<StreetEntity> getAllFromCity(String cityID, Integer firstResult, Integer maxResults);

    Stream<StreetEntity> getAllInCategory(StreetCategory streetCategory, Integer firstResult, Integer maxResults);

    boolean deleteStreet(StreetEntity streetEntity);

    boolean deleteStreetByID(Long id);

    StreetEntity update(StreetEntity streetEntity);
}
