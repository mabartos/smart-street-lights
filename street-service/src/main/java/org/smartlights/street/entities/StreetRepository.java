package org.smartlights.street.entities;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.smartlights.street.utils.StreetCategory;

import javax.persistence.TypedQuery;
import java.util.stream.Stream;

public class StreetRepository implements PanacheRepository<StreetEntity>, StreetRepositoryModel {
    public static final int PAGE_COUNT = 100;

    @Override
    public StreetEntity create(StreetEntity streetEntity) {
        try {
            persistAndFlush(streetEntity);
            return getByName(streetEntity.streetName);
        } catch (ConstraintViolationException e) {
            return null; //todo - temporary solution to respect the signature
        }
    }

    @Override
    public StreetEntity getById(Long id) {
        return find("streetId", id).firstResult();
    }

    @Override
    public StreetEntity getByName(String name) {
        return find("streetName", name).firstResult();
    }

    @Override
    public Stream<StreetEntity> getAll(Integer firstResult, Integer maxResults) {
        PanacheQuery<StreetEntity> query = findAll();
        query = query.page(firstResult != null ? firstResult : 0, maxResults != null ? maxResults : PAGE_COUNT);
        return query.list().stream();
    }

    @Override
    public Stream<StreetEntity> getAllFromCity(String cityID, Integer firstResult, Integer maxResults) {
        TypedQuery<StreetEntity> query = getEntityManager().createNamedQuery("getStreetsFromCity", StreetEntity.class);
        query.setParameter("cityId", cityID);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList().stream();
    }

    @Override
    public Stream<StreetEntity> getAllInCategory(StreetCategory streetCategory, Integer firstResult, Integer maxResults) {
        TypedQuery<StreetEntity> query = getEntityManager().createNamedQuery("getStreetsByCategory", StreetEntity.class);
        query.setParameter("streetCategory", streetCategory);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList().stream();
    }

    @Override
    public boolean deleteStreet(StreetEntity streetEntity) {
        if (isPersistent(streetEntity)) {
            delete(streetEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteStreetByID(Long id) {
        return deleteById(id);
    }

    @Override
    public StreetEntity update(StreetEntity streetEntity) {
        return Panache.getEntityManager().merge(streetEntity);
    }
}
