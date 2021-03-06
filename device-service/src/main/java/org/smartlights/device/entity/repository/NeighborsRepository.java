package org.smartlights.device.entity.repository;

import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.EntityUtils;
import org.smartlights.device.entity.repository.model.NeighborsRepositoryModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional
@ApplicationScoped
public class NeighborsRepository implements NeighborsRepositoryModel {

    @Inject
    DeviceRepository deviceRepository;

    @Override
    public Stream<Long> getAllID(Long parentID) {
        return getAllID(parentID, null, null);
    }

    @Override
    public Stream<DeviceEntity> getAll(Long parentID) {
        return getAll(parentID, null, null);
    }

    @Override
    public Stream<Long> getAllID(Long parentID, Integer firstResult, Integer maxResults) {
        return getAll(parentID, firstResult, maxResults).map(f -> f.id);
    }

    @Override
    public Stream<DeviceEntity> getAll(Long parentID, Integer firstResult, Integer maxResults) {
        return Optional.ofNullable(deviceRepository.getByID(parentID))
                .map(f -> f.neighbors)
                .map(Collection::stream)
                .map(f -> EntityUtils.pagination(f, firstResult, maxResults))
                .orElseGet(Stream::empty);
    }

    @Override
    public DeviceEntity getByID(Long parentID, Long id) {
        return Optional.ofNullable(deviceRepository.getByID(id))
                .filter(f -> f.parent != null)
                .filter(f -> f.parent.id.equals(parentID))
                .orElse(null);
    }

    @Override
    public boolean addNeighbor(Long parentID, Long id) {
        DeviceEntity parent = deviceRepository.getByID(parentID);
        DeviceEntity deviceEntity = deviceRepository.getByID(id);

        if (deviceEntity != null && parent != null) {
            deviceEntity.parent.id = parentID;
            return parent.neighbors.add(deviceEntity);
        }
        return false;
    }

    @Override
    public boolean removeNeighbor(Long parentID, Long id) {
        DeviceEntity parent = deviceRepository.getByID(parentID);
        DeviceEntity deviceEntity = deviceRepository.getByID(id);

        if (deviceEntity != null && parent != null) {
            deviceEntity.parent = null;
            return parent.neighbors.remove(deviceEntity);
        }
        return false;
    }

    @Override
    public Integer getNeighborsCount(Long parentID) {
        DeviceEntity parent = deviceRepository.getByID(parentID);
        return parent != null ? parent.neighbors.size() : 0;
    }
}
