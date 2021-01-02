package org.smartlights.user.entity.repository.model;

import org.smartlights.user.entity.UserEntity;

import java.util.stream.Stream;

public interface UserRepositoryModel {

    Integer PAGE_COUNT = 50;

    Stream<UserEntity> getAll(Integer firstResult, Integer maxResults);

    Stream<UserEntity> getAll();

    UserEntity create(UserEntity user);

    UserEntity getByID(Long userID);

    UserEntity getByUsername(String username);

    UserEntity update(UserEntity user);

    boolean removeByID(Long userID);
}
