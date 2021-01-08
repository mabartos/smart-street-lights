package org.smartlights.device.entity.repository.model;

import org.smartlights.device.entity.DeviceEntity;

import java.util.stream.Stream;

/**
 * Define Neighbors abilities from Device DB
 */
public interface NeighborsRepositoryModel {

    /**
     * Get all parent neighbor IDs
     *
     * @param parentID parent ID
     * @return stream of IDs of neihbours
     */
    Stream<Long> getAllID(Long parentID);

    /**
     * Get all parent neighbors
     *
     * @param parentID parent ID
     * @return stream of neighbors
     */
    Stream<DeviceEntity> getAll(Long parentID);

    /**
     * Get all neighbors by ID
     *
     * @param firstResult index of the first required device
     * @param maxResults  count of results from the firstResult
     * @return stream of device IDs
     */
    Stream<Long> getAllID(Long parentID, Integer firstResult, Integer maxResults);

    /**
     * Get all neighbors
     *
     * @param firstResult index of the first required device
     * @param maxResults  count of results from the firstResult
     * @return stream of devices
     */
    Stream<DeviceEntity> getAll(Long parentID, Integer firstResult, Integer maxResults);

    /**
     * Get neighbor by ID and parent ID
     *
     * @param parentID
     * @param id
     * @return device representation
     */
    DeviceEntity getByID(Long parentID, Long id);

    /**
     * Add neighbor to parent
     *
     * @param parentID
     * @param id
     * @return state
     */
    boolean addNeighbor(Long parentID, Long id);

    /**
     * Remove neighbor from parent
     *
     * @param parentID
     * @param id
     * @return state
     */
    boolean removeNeighbor(Long parentID, Long id);

    /**
     * Get neighbors count
     *
     * @param parentID
     * @return count of neighbors
     */
    Integer getNeighborsCount(Long parentID);
}
