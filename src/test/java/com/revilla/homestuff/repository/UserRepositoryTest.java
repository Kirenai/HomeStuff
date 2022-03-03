package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    private User userOne;
    private User userTwo;
    private User userThree;

    @BeforeEach
    void init() {
        Long userIdOne = 1L;
        String usernameOne = "userOne";
        String passwordOne = "passwordOne";
        String firstName = "firstName";
        String lastName = "lastName";
        Byte age = 20;

        Long userIdTwo = 2L;
        String usernameTwo = "userTwo";
        String passwordTwo = "passwordTwo";
        String firstNameTwo = "firstNameTwo";
        String lastNameTwo = "lastNameTwo";
        Byte ageTwo = 30;

        Long userIdThree = 3L;
        String usernameThree = "userThree";
        String passwordThree = "passwordThree";
        String firstNameThree = "firstNameThree";
        String lastNameThree = "lastNameThree";
        Byte ageThree = 40;

        this.userOne = UserServiceDataTestUtils.getMockUser(userIdOne, usernameOne,
                passwordOne, firstName, lastName, age);
        this.userTwo = UserServiceDataTestUtils.getMockUser(userIdTwo, usernameTwo,
                passwordTwo, firstNameTwo, lastNameTwo, ageTwo);
        this.userThree = UserServiceDataTestUtils.getMockUser(userIdThree, usernameThree,
                passwordThree, firstNameThree, lastNameThree, ageThree);
    }

    @AfterEach
    void tearDown() {
        this.entityManager.createNativeQuery("ALTER TABLE USERS ALTER COLUMN user_id RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("Should find a user when find by username")
    void shouldFindUserWhenFindByUsername() {
        String usernameToFind = "userOne";

        this.userRepository.save(this.userOne);
        User userFound = this.userRepository.findByUsername(usernameToFind).orElseThrow();

        Assertions.assertEquals(usernameToFind, userFound.getUsername());
    }

    @Test
    @DisplayName("Should check if a user exists when exists by username")
    void shouldVerifiesIfUserExistsWhenExistsByUsername() {
        String usernameToFind = "userOne";

        this.userRepository.save(this.userOne);
        boolean exists = this.userRepository.existsByUsername(usernameToFind);

        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Should find a user list when finding all")
    void shouldFindUserPageWhenFindingAll() {
        int sizeExcepted = 3;
        Pageable pageableMock = Mockito.mock(Pageable.class);

        this.userRepository.saveAll(Arrays.asList(this.userOne, this.userTwo, this.userThree));
        List<User> userList = this.userRepository.findAll(pageableMock).getContent();

        Assertions.assertEquals(sizeExcepted, userList.size());
    }

    @Test
    @DisplayName("Should find user by id when find one")
    void shouldFindUserByIdWhenFindOne() {
        Long userIdToFind = 1L;

        this.userRepository.save(this.userOne);
        User userFound = this.userRepository.findById(userIdToFind).orElseThrow();

        Assertions.assertEquals(userIdToFind, userFound.getUserId());
    }

    @Test
    @DisplayName("Should save a user when saved")
    void shouldSaveUserWhenSaved() {
        User userSaved = this.userRepository.save(this.userOne);

        Assertions.assertEquals(this.userOne.getFirstName(), userSaved.getFirstName());
        Assertions.assertEquals(this.userOne.getLastName(), userSaved.getLastName());
        Assertions.assertEquals(this.userOne.getUserId(), userSaved.getUserId());
    }

    @Test
    @DisplayName("Should update a user when updated")
    void shouldUpdateUserWhenUpdated() {
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";

        this.userRepository.save(this.userOne);
        User userToUpdate = this.userRepository.findById(this.userOne.getUserId())
                .orElseThrow();
        userToUpdate.setFirstName(newFirstName);
        userToUpdate.setLastName(newLastName);
        User userUpdated = this.userRepository.save(userToUpdate);

        Assertions.assertEquals(newFirstName, userUpdated.getFirstName());
        Assertions.assertEquals(newLastName, userUpdated.getLastName());
    }

    @Test
    @DisplayName("Should delete a user when deleted")
    void shouldDeleteUserWhenDeleted() {
        this.userRepository.save(this.userOne);
        this.userRepository.delete(this.userOne);
        boolean exists = this.userRepository.existsById(this.userOne.getUserId());

        Assertions.assertFalse(exists);
    }

}