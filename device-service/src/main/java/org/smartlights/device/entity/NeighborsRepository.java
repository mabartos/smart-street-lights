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
        return Optional.ofNullable(deviceRepository.getByID(parentID).neighborsID.stream())
                .orElseGet(Stream::empty);
    }

    @Override
    public Stream<Device> getAll(Long parentID) {
        return Optional.ofNullable(deviceRepository.getAllFromSetID(getAllID(parentID)))
                .orElseGet(Stream::empty);
    }

    @Override
    public Device getByID(Long parentID, Long id) {
        Device device = deviceRepository.getByID(id);
        return Optional.ofNullable(device)
                .filter(f -> f.parentID.equals(parentID))
                .orElse(null);
    }

    @Override
    public boolean addNeighbor(Long parentID, Long id) {
        Device parent = deviceRepository.getByID(parentID);
        Device device = deviceRepository.getByID(id);

        if (device != null && parent != null) {
            device.parentID = parentID;
            return parent.neighborsID.add(id);
        }
        return false;
    }

    @Override
    public boolean removeNeighbor(Long parentID, Long id) {
        Device parent = deviceRepository.getByID(parentID);
        Device device = deviceRepository.getByID(id);

        if (device != null && parent != null) {
            device.parentID = null;
            return parent.neighborsID.remove(id);
        }
        return false;
    }

    @Override
    public Integer getNeighborsCount(Long parentID) {
        Device parent = deviceRepository.getByID(parentID);
        return parent != null ? parent.neighborsID.size() : 0;
    }
}
