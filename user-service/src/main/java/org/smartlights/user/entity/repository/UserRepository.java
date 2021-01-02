package org.smartlights.user.entity.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.smartlights.user.entity.UserEntity;
import org.smartlights.user.entity.repository.model.UserRepositoryModel;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.stream.Stream;

@Transactional
@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity>, UserRepositoryModel {

    @Override
    public Stream<UserEntity> getAll(Integer firstResult, Integer maxResults) {
        PanacheQuery<UserEntity> query = findAll();
        query = query.page(firstResult != null ? firstResult : 0, maxResults != null ? maxResults : PAGE_COUNT);
        return query.list().stream();
    }

    @Override
    public Stream<UserEntity> getAll() {
        return getAll(null, null);
    }

    @Override
    public UserEntity create(UserEntity user) {
        try {
            persistAndFlush(user);
            return getByUsername(user.getUsername());
        } catch (ConstraintViolationException e) {
            return null;
        }
    }

    @Override
    public UserEntity getByID(Long userID) {
        return userID != null ? findById(userID) : null;
    }

    @Override
    public UserEntity getByUsername(String username) {
        try {
            TypedQuery<UserEntity> query = getEntityManager().createNamedQuery("getUserByUsername", UserEntity.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserEntity update(UserEntity user) {
        return getEntityManager().merge(user);
    }

    @Override
    public boolean removeByID(Long userID) {
        return deleteById(userID);
    }
}
