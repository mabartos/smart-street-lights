package org.smartlights.city.entities;

import org.smartlights.city.entities.CityEntity;

import java.util.stream.Stream;

public interface CityRepositoryModel {

    CityEntity create(CityEntity cityEntity);

    CityEntity getById(Long id);

    CityEntity getByName(String name);

    Stream<CityEntity> getAll(Integer firstResult, Integer maxResults);

    boolean deleteCity(CityEntity cityEntity);

    boolean deleteCityByID(Long id);

    CityEntity update(CityEntity cityEntity);
}
