package org.smartlights.user.entity.repository.model;

import org.smartlights.user.entity.UserEntity;

import java.util.stream.Stream;

/**
 * Define User DB abilities
 */
public interface UserRepositoryModel {

    /**
     * Default count of results return by bulk operations
     */
    Integer PAGE_COUNT = 50;

    /**
     * Get all Users
     *
     * @param firstResult index of the first required user
     * @param maxResults  count of results from the firstResult
     * @return stream of users
     */
    Stream<UserEntity> getAll(Integer firstResult, Integer maxResults);

    /**
     * Get all Users
     *
     * @return stream of users
     */
    Stream<UserEntity> getAll();

    /**
     * Create user
     *
     * @param user user representation
     * @return created user
     */
    UserEntity create(UserEntity user);

    /**
     * Get user by ID
     *
     * @param userID
     * @return user representation
     */
    UserEntity getByID(Long userID);

    /**
     * Get user by username
     *
     * @param username
     * @return user representation
     */
    UserEntity getByUsername(String username);

    /**
     * Update user
     *
     * @param user user representation
     * @return user representation
     */
    UserEntity update(UserEntity user);

    /**
     * Remove user by ID
     *
     * @param userID
     * @return true if success, otherwise false
     */
    boolean removeByID(Long userID);
}
