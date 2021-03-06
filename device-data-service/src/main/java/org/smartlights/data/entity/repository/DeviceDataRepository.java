package org.smartlights.data.entity.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.smartlights.data.dto.DataSerializer;
import org.smartlights.data.dto.DeviceDataDTO;
import org.smartlights.data.entity.DeviceDataEntity;
import org.smartlights.data.entity.repository.model.DeviceDataRepositoryModel;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.smartlights.data.utils.EntityUtils.pagination;

@ApplicationScoped
@Transactional
public class DeviceDataRepository implements PanacheRepository<DeviceDataEntity>, DeviceDataRepositoryModel {

    @Override
    public boolean saveData(Long deviceID, DeviceDataDTO data) {
        Long foundDeviceID = Optional.ofNullable(deviceID).orElseGet(() -> data.deviceID);
        if (foundDeviceID == null) return false;

        DeviceDataEntity dataEntity = DataSerializer.modelToEntity(data);
        dataEntity.deviceID = foundDeviceID;
        dataEntity.persist();
        return true;
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
    public Stream<DeviceDataEntity> getAllFromDevice(Long deviceID) {
        return getAllFromDevice(deviceID, null, null);
    }

    @Override
    public Stream<DeviceDataEntity> getAllFromDevice(Long deviceID, Integer firstResult, Integer maxResults) {
        try {
            TypedQuery<DeviceDataEntity> query = getEntityManager().createNamedQuery("getAllByDeviceID", DeviceDataEntity.class);
            query.setParameter("deviceID", deviceID);
            return pagination(query, firstResult, maxResults).getResultList().stream();
        } catch (NoResultException e) {
            return Stream.empty();
        }
    }

    @Override
    public Stream<DeviceDataEntity> getAllRecentThan(Long deviceID, Date date) {
        return getAllRecentThan(deviceID, date, null, null);
    }

    @Override
    public Stream<DeviceDataEntity> getAllRecentThan(Long deviceID, Date date, Integer firstResult, Integer maxResults) {
        return pagination(getAllFromDevice(deviceID)
                .filter(f -> f.timestamp.after(date)), firstResult, maxResults);
    }

    @Override
    public Stream<DeviceDataEntity> getAllOlderThan(Long deviceID, Date date) {
        return getAllOlderThan(deviceID, date, null, null);
    }

    @Override
    public Stream<DeviceDataEntity> getAllOlderThan(Long deviceID, Date date, Integer firstResult, Integer maxResults) {
        return pagination(getAllFromDevice(deviceID)
                .filter(f -> f.timestamp.before(date)), firstResult, maxResults);
    }

    @Override
    public DeviceDataEntity getByID(Long id) {
        return id != null ? findById(id) : null;
    }

    @Override
    public boolean removeByID(Long id) {
        return id != null && deleteById(id);
    }

    @Override
    public boolean removeByIDs(Set<Long> ids) {
        Query query = getEntityManager().createNamedQuery("removeDeviceDataByIDs");
        query.setParameter("ids", ids);
        return query.executeUpdate() != 0;
    }

    @Override
    public boolean removeOlderThan(Long deviceID, Date date) {
        Set<Long> ids = getAllOlderThan(deviceID, date).map(f -> f.id).collect(Collectors.toSet());
        return removeByIDs(ids);
    }

    @Override
    public Long getCountOfDeviceData(Long deviceID) {
        TypedQuery<Long> query = getEntityManager().createNamedQuery("getCountOfDeviceData", Long.class);
        query.setParameter("deviceID", deviceID);
        return query.getSingleResult();
    }
}
