package org.smartlights;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartlights.user.data.UserRole;
import org.smartlights.user.entity.UserEntity;
import org.smartlights.user.entity.repository.UserRepository;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class UserModelTest {

    @Inject
    UserRepository repository;

    @Test
    public void testGetUsers() {
        final long usersCount = repository.getAll().count();
        final int createUsersCount = 3;
        assertEquals(createUsers(createUsersCount).size(), createUsersCount);
        assertEquals(usersCount + createUsersCount, repository.getAll().count());
    }

    @Test
    public void testCreateUser() {
        final Integer uniqueNumber = 5;
        UserEntity user = createUser(uniqueNumber);
        assertNotNull(user);
        assertEquals("user" + uniqueNumber, user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("user" + uniqueNumber + "@userservice.com", user.getEmail());
        Assertions.assertEquals(Stream.of(UserRole.USER).collect(Collectors.toSet()), user.getRoles());
    }

    @Test
    public void testRemoveUser() {
        final int createUsersCount = 3;
        long usersCount = repository.getAll().count();

        Set<UserEntity> users = createUsers(createUsersCount);
        usersCount += createUsersCount;
        assertEquals(usersCount, repository.getAll().count());

        UserEntity user = users.stream().findFirst().orElse(null);
        assertNotNull(user);
        assertTrue(repository.removeByID(user.id));
        assertEquals(usersCount - 1, repository.getAll().count());
    }

    @Test
    public void testUpdateUser() {
        final int uniqueInt = new Random().nextInt();
        final String updateAttr = "Update";

        UserEntity user = createUser(uniqueInt);
        assertNotNull(user);

        user.setUsername(updateAttr);
        user.setFirstName(updateAttr);
        user.setLastName(updateAttr);
        user.setEmail(updateAttr + "@service.com");
        Set<String> roles = new HashSet<>();
        roles.add(UserRole.ADMIN);
        user.setRoles(roles);

        UserEntity updated = repository.update(user);
        assertNotNull(updated);
        assertCreatedUser(user, updated);
    }

    private UserEntity createUser() {
        return createUser(null);
    }

    private UserEntity createUser(Integer uniqueNumber) {
        Random random = new Random();
        final int randomNumber = uniqueNumber != null ? uniqueNumber : random.nextInt();
        UserEntity user = new UserEntity();
        user.setUsername("user" + randomNumber);
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("user" + randomNumber + "@userservice.com");
        return repository.create(user);
    }

    private void assertCreatedUser(UserEntity expected, UserEntity actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getRoles(), actual.getRoles());
    }

    private Set<UserEntity> createUsers(int count) {
        Set<UserEntity> users = new HashSet<>();
        for (int i = 0; i < count; i++) {
            UserEntity user = createUser();
            assertNotNull(user);
            users.add(user);
        }
        return users;
    }
}
