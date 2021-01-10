package org.smartlights.device.entity.repository;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.repository.model.DeviceRepositoryModel;
import org.smartlights.device.utils.ConflictException;
import org.smartlights.device.utils.Constants;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.smartlights.device.entity.EntityUtils.pagination;

@Transactional
@ApplicationScoped
public class DeviceRepository implements PanacheRepository<DeviceEntity>, DeviceRepositoryModel {

    @Override
    public DeviceEntity create(DeviceEntity deviceEntity) {
        try {
            persistAndFlush(deviceEntity);
            return getBySerialNo(deviceEntity.serialNo);
        } catch (ConstraintViolationException e) {
            throw new ConflictException(e.getConstraintName());
        }
    }

    @Override
    public DeviceEntity getByID(Long id) {
        try {
            TypedQuery<DeviceEntity> entity = getEntityManager().createNamedQuery("getDeviceByID", DeviceEntity.class);
            entity.setParameter("id", id);
            return entity.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public DeviceEntity getWholeByID(Long id) {
        try {
            TypedQuery<DeviceEntity> entity = getEntityManager().createNamedQuery("getWholeDevice", DeviceEntity.class);
            entity.setParameter("id", id);
            return entity.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public DeviceEntity getBySerialNo(String serialNo) {
        TypedQuery<DeviceEntity> query = getEntityManager().createNamedQuery("getDeviceBySerialNo", DeviceEntity.class);
        query.setParameter("serialNo", serialNo);
        return query.getSingleResult();
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
        query.setParameter(Constants.STREET_ID_PARAM, streetID);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        pagination(query, firstResult, maxResults);

        return query.getResultList().stream();
    }

    @Override
    public Stream<DeviceEntity> getAllFromCity(String cityID, Integer firstResult, Integer maxResults) {
        TypedQuery<DeviceEntity> query = getEntityManager().createNamedQuery("getDevicesFromCity", DeviceEntity.class);
        query.setParameter(Constants.CITY_ID_PARAM, cityID);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        pagination(query, firstResult, maxResults);

        return query.getResultList().stream();
    }

    @Override
    public Stream<DeviceEntity> getAll(Integer firstResult, Integer maxResults) {
        PanacheQuery<DeviceEntity> query = findAll();
        query = query.page(firstResult != null ? firstResult : 0, maxResults != null ? maxResults : PAGE_COUNT);
        return query.list().stream();
    }

    @Override
    public boolean deleteDevice(DeviceEntity deviceEntity) {
        if (isPersistent(deviceEntity)) {
            delete(deviceEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByID(Long id) {
        return deleteById(id);
    }

    @Override
    public DeviceEntity update(DeviceEntity deviceEntity) {
        return Panache.getEntityManager().merge(deviceEntity);
    }
}
