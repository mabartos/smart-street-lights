package org.smartlights.device.entity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional
@ApplicationScoped
public class NeighborsRepository implements NeighborsRepositoryModel {

    @Inject
    DeviceRepository deviceRepository;

    @Override
    public Stream<Long> getAllID(Long parentID) {
        return Optional.of(deviceRepository.getByID(parentID).neighbors.stream().map(f -> f.id))
                .orElseGet(Stream::empty);
    }

    @Override
    public Stream<DeviceEntity> getAll(Long parentID) {
        return Optional.ofNullable(deviceRepository.getAllFromSetID(getAllID(parentID)))
                .orElseGet(Stream::empty);
    }

    @Override
    public DeviceEntity getByID(Long parentID, Long id) {
        DeviceEntity deviceEntity = deviceRepository.getByID(id);
        return Optional.ofNullable(deviceEntity)
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
