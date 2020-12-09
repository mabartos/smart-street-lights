package org.smartlights.device.entity;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.stream.Stream;

@ApplicationScoped
public class DeviceRepository implements PanacheRepository<Device>, DeviceRepositoryModel {

    public Device create(Device device) {
        persistAndFlush(device);
        return getBySerialNo(device.serialNo);
    }

    public Device getByID(Long id) {
        return findById(id);
    }

    public Device getBySerialNo(String serialNo) {
        return find("serialNo", serialNo).firstResult();
    }

    public Stream<Device> getAllFromStreet(String streetID, Integer firstResult, Integer maxResults) {
        TypedQuery<Device> query = getEntityManager().createNamedQuery("getDevicesFromStreet", Device.class);
        query.setParameter("streetID", streetID);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        pagination(query, firstResult, maxResults);

        return query.getResultList().stream();
    }

    @Override
    public Stream<Device> getAllFromCity(String cityID, Integer firstResult, Integer maxResults) {
        TypedQuery<Device> query = getEntityManager().createNamedQuery("getDevicesFromCity", Device.class);
        query.setParameter("cityID", cityID);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        pagination(query, firstResult, maxResults);

        return query.getResultList().stream();
    }

    public Stream<Device> getAll(Integer firstResult, Integer maxResults) {
        PanacheQuery<Device> query = findAll();
        if (firstResult != null && maxResults != null) {
            query.page(firstResult, maxResults);
        }
        return query.stream();
    }

    public boolean deleteDevice(Device device) {
        if (isPersistent(device)) {
            delete(device);
            return true;
        }
        return false;
    }

    public boolean deleteByID(Long id) {
        return deleteById(id);
    }

    public Device update(Device device) {
        return Panache.getEntityManager().merge(device);
    }

    private static TypedQuery<Device> pagination(TypedQuery<Device> query, Integer firstResult, Integer maxResults) {
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query;
    }
}
