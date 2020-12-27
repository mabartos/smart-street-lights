package org.smartlights.street.entities;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.hibernate.exception.ConstraintViolationException;

import java.util.stream.Stream;

public class CityRepository implements PanacheRepository<CityEntity>, CityRepositoryModel {
    public static final int PAGE_COUNT = 100;

    @Override
    public CityEntity create(CityEntity cityEntity) {
        try {
            persistAndFlush(cityEntity);
            return getByName(cityEntity.cityName);
        } catch (ConstraintViolationException e) {
            return null; //todo - temporary solution to respect the signature
        }
    }

    @Override
    public CityEntity getById(Long id) {
        return find("cityId", id).firstResult();
    }

    @Override
    public CityEntity getByName(String name) {
        return find("cityName", name).firstResult();
    }

    @Override
    public Stream<CityEntity> getAll(Integer firstResult, Integer maxResults) {
        PanacheQuery<CityEntity> query = findAll();
        query = query.page(firstResult != null ? firstResult : 0, maxResults != null ? maxResults : PAGE_COUNT);
        return query.list().stream();
    }

    @Override
    public boolean deleteCity(CityEntity cityEntity) {
        if (isPersistent(cityEntity)) {
            delete(cityEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCityByID(Long id) {
        return deleteById(id);
    }

    @Override
    public CityEntity update(CityEntity cityEntity) {
        return Panache.getEntityManager().merge(cityEntity);
    }
}
