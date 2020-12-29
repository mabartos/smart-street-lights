package org.smartlights.device.entity.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.smartlights.device.dto.DeviceDataDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.DeviceDataEntity;
import org.smartlights.device.entity.DeviceEntity;
import org.smartlights.device.entity.repository.model.DeviceDataRepositoryModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.smartlights.device.entity.EntityUtils.pagination;

@ApplicationScoped
@Transactional
public class DeviceDataRepository implements PanacheRepository<DeviceDataEntity>, DeviceDataRepositoryModel {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    DeviceSerializer serializer;

    @Override
    public boolean saveData(Long parentID, DeviceDataDTO data) {
        DeviceEntity device = deviceRepository.getByIDWithData(Optional.ofNullable(parentID).orElseGet(() -> data.deviceID));
        if (device == null) return false;

        DeviceDataEntity dataEntity = serializer.modelToEntity(data);
        dataEntity.device = device;
        dataEntity.persist();
        device.data.add(dataEntity);
        checkAndRemoveDataSize(device.data, device);
        return true;
    }

    //TODO fix this
    private boolean checkAndRemoveDataSize(Set<DeviceDataEntity> data, DeviceEntity device) {
        return !data.isEmpty()
                && data.size() >= DATA_SIZE
                && data.remove(data.stream().findFirst().get())
                && getEntityManager().merge(device) != null;
    }

    @Override
    public boolean saveData(DeviceDataDTO data) {
        return saveData(null, data);
    }

    @Override
    public boolean saveData(DeviceDataEntity data) {
        persistAndFlush(data);
        return true;
    }

    @Override
    public Stream<DeviceDataEntity> getAllFromParent(Long parentID) {
        return getAllFromParent(parentID, null, null);
    }

    @Override
    public Stream<DeviceDataEntity> getAllFromParent(Long parentID, Integer firstResult, Integer maxResults) {
        Stream<DeviceDataEntity> stream = Optional.ofNullable(deviceRepository.getByIDWithData(parentID))
                .map(f -> f.data)
                .map(Collection::stream)
                .orElseGet(Stream::empty);

        return pagination(stream, firstResult, maxResults);
    }

    @Override
    public Stream<DeviceDataEntity> getAllRecentThan(Long parentID, Timestamp timestamp) {
        return getAllRecentThan(parentID, timestamp, null, null);
    }

    @Override
    public Stream<DeviceDataEntity> getAllRecentThan(Long parentID, Timestamp timestamp, Integer firstResult, Integer maxResults) {
        return pagination(getAllFromParent(parentID)
                .filter(f -> f.timestamp.after(timestamp)), firstResult, maxResults);
    }

    @Override
    public Stream<DeviceDataEntity> getAllOlderThan(Long parentID, Timestamp timestamp) {
        return getAllOlderThan(parentID, timestamp, null, null);
    }

    @Override
    public Stream<DeviceDataEntity> getAllOlderThan(Long parentID, Timestamp timestamp, Integer firstResult, Integer maxResults) {
        return pagination(getAllFromParent(parentID)
                .filter(f -> f.timestamp.before(timestamp)), firstResult, maxResults);
    }

    @Override
    public DeviceDataEntity getByID(Long id) {
        return id != null ? findById(id) : null;
    }

    @Override
    public DeviceDataEntity getByIDWithParent(Long id) {
        try {
            TypedQuery<DeviceDataEntity> query = getEntityManager().createNamedQuery("getDeviceDataWithParent", DeviceDataEntity.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean removeByID(Long id) {
        return id != null && deleteById(id);
    }

    @Override
    public boolean removeOlderThan(Long parentID, Timestamp timestamp) {
        Set<Long> ids = getAllOlderThan(parentID, timestamp).map(f -> f.id).collect(Collectors.toSet());
        Query query = getEntityManager().createNamedQuery("removeDeviceDataByIDs");
        query.setParameter("ids", ids);
        return query.executeUpdate() != 0;
    }

}
