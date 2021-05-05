package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    void shouldFindUserByFirstName() {

        int expectedListLength = 1;
        User persistedUser = entityManager.persist(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), " ",
                " ");

        assertEquals(expectedListLength, users.size());
        assertEquals(persistedUser.getId(), users.get(0)
                                                 .getId());
    }

    @Test
    void shouldFindUserByLastName() {

        int expectedListLength = 1;
        User persistedUser = entityManager.persist(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ",
                persistedUser.getLastName(), " ");

        assertEquals(expectedListLength, users.size());
        assertEquals(persistedUser.getId(), users.get(0)
                                                 .getId());
    }

    @Test
    void shouldFindUserByPartOfLastName() {

        int expectedListLength = 1;
        int substringBeginIndex = 2;
        int substringEndIndex = 6;
        User persistedUser = entityManager.persist(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ",
                persistedUser.getLastName()
                             .substring(substringBeginIndex, substringEndIndex),
                " ");

        assertEquals(expectedListLength, users.size());
        assertEquals(persistedUser.getId(), users.get(0)
                                                 .getId());
    }

    @Test
    void shouldFindUserByEmailName() {

        int expectedListLength = 1;
        User persistedUser = entityManager.persist(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", " ",
                persistedUser.getEmail());

        assertEquals(expectedListLength, users.size());
        assertEquals(persistedUser.getId(), users.get(0)
                                                 .getId());
    }

    @Test
    void shouldNotFindUser() {

        int expectedListLength = 0;
        entityManager.persist(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", " ", " ");

        assertEquals(expectedListLength, users.size());
    }

}
