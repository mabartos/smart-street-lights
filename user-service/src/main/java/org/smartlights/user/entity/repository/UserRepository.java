package org.smartlights.user.entity.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.smartlights.user.data.UserRole;
import org.smartlights.user.entity.UserEntity;
import org.smartlights.user.entity.repository.model.UserRepositoryModel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Transactional
@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity>, UserRepositoryModel {

    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());

    @ConfigProperty(name = "createAdmin", defaultValue = "false")
    Boolean createAdminUser;

    public void createAdmin(@Observes StartupEvent start) {
        if (!createAdminUser) return;

        UserEntity entity = new UserEntity();
        entity.setUsername("admin");
        entity.setPassword("admin");
        entity.setFirstName("Admin");
        entity.setLastName("Admin");
        entity.setEmail("admin@admin.com");
        Set<String> roles = new HashSet<>();
        roles.add(UserRole.SYS_ADMIN);
        roles.add(UserRole.ADMIN);
        entity.setRoles(roles);

        final String message = Optional.ofNullable(create(entity)).map(f -> "Admin created.").orElse("Cannot create admin user.");
        logger.info(message);
    }

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
        persistAndFlush(user);
        return getByUsername(user.getUsername());
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
