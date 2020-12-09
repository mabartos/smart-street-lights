package org.smartlights.device.services;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.smartlights.device.entity.Device;
import org.smartlights.device.entity.DeviceRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.Stream;

@ApplicationScoped
public class DeviceService {

    @Inject
    DeviceRepository repository;

    public void create(Device device) {
        repository.persistAndFlush(device);
    }

    public Device getByID(Long id) {
        return repository.findById(id);
    }

    public Stream<Device> getAll(Integer firstResult, Integer maxResults) {
        PanacheQuery<Device> query = repository.findAll();
        if (firstResult != null && maxResults != null) {
            query.page(firstResult, maxResults);
        }
        return query.stream();
    }

    public boolean delete(Device device) {
        if (repository.isPersistent(device)) {
            repository.delete(device);
            return true;
        }
        return false;
    }

    public boolean deleteByID(Long id) {
        return repository.deleteById(id);
    }

    public Device update(Device device) {
        return Panache.getEntityManager().merge(device);
    }
}
