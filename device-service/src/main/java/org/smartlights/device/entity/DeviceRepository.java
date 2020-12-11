package org.smartlights.device.entity;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@ApplicationScoped
public class DeviceRepository implements PanacheRepository<DeviceEntity>, DeviceRepositoryModel {

    public DeviceEntity create(DeviceEntity deviceEntity) {
        persistAndFlush(deviceEntity);
        return getBySerialNo(deviceEntity.serialNo);
    }

    public DeviceEntity getByID(Long id) {
        return findById(id);
    }

    public DeviceEntity getBySerialNo(String serialNo) {
        return find("serialNo", serialNo).firstResult();
    }

    @Override
    public Stream<DeviceEntity> getAllFromSetID(Set<Long> deviceSet) {
        return list("id in ?1", deviceSet).stream();
    }

    @Override
    public Stream<DeviceEntity> getAllFromSetID(Stream<Long> deviceSet) {
        return getAllFromSetID(deviceSet.collect(Collectors.toSet()));
    }

    public Stream<DeviceEntity> getAllFromStreet(String streetID, Integer firstResult, Integer maxResults) {
        TypedQuery<DeviceEntity> query = getEntityManager().createNamedQuery("getDevicesFromStreet", DeviceEntity.class);
        query.setParameter("streetID", streetID);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        pagination(query, firstResult, maxResults);

        return query.getResultList().stream();
    }

    @Override
    public Stream<DeviceEntity> getAllFromCity(String cityID, Integer firstResult, Integer maxResults) {
        TypedQuery<DeviceEntity> query = getEntityManager().createNamedQuery("getDevicesFromCity", DeviceEntity.class);
        query.setParameter("cityID", cityID);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        pagination(query, firstResult, maxResults);

        return query.getResultList().stream();
    }

    public Stream<DeviceEntity> getAll(Integer firstResult, Integer maxResults) {
        PanacheQuery<DeviceEntity> query = findAll();
        if (firstResult != null && maxResults != null) {
            query.page(firstResult, maxResults);
        }
        return query.stream();
    }

    public boolean deleteDevice(DeviceEntity deviceEntity) {
        if (isPersistent(deviceEntity)) {
            delete(deviceEntity);
            return true;
        }
        return false;
    }

    public boolean deleteByID(Long id) {
        return deleteById(id);
    }

    public DeviceEntity update(DeviceEntity deviceEntity) {
        return Panache.getEntityManager().merge(deviceEntity);
    }

    private static TypedQuery<DeviceEntity> pagination(TypedQuery<DeviceEntity> query, Integer firstResult, Integer maxResults) {
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query;
    }
}
