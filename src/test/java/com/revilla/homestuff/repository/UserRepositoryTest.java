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

    private User funwiz;
    private User shande;
    private User jk;

    @BeforeEach
    void init() {
        String usernameOne = "userOne";
        String passwordOne = "passwordOne";
        String firstName = "firstName";
        String lastName = "lastName";
        Byte age = 20;

        String usernameTwo = "userTwo";
        String passwordTwo = "passwordTwo";
        String firstNameTwo = "firstNameTwo";
        String lastNameTwo = "lastNameTwo";
        Byte ageTwo = 30;

        String usernameThree = "userThree";
        String passwordThree = "passwordThree";
        String firstNameThree = "firstNameThree";
        String lastNameThree = "lastNameThree";
        Byte ageThree = 40;

        this.funwiz = UserServiceDataTestUtils.getMockUser(usernameOne,
                passwordOne, firstName, lastName, age);
        this.shande = UserServiceDataTestUtils.getMockUser(usernameTwo,
                passwordTwo, firstNameTwo, lastNameTwo, ageTwo);
        this.jk = UserServiceDataTestUtils.getMockUser(usernameThree,
                passwordThree, firstNameThree, lastNameThree, ageThree);
    }

    @Test
    @DisplayName("Should find a user when find by username")
    void shouldFindUserWhenFindByUsername() {
        String username = this.userRepository.save(this.funwiz)
                .getUsername();
        User userFound = this.userRepository.findByUsername(username)
                .orElseThrow();

        Assertions.assertEquals(username, userFound.getUsername());
    }

    @Test
    @DisplayName("Should check if a user exists when exists by username")
    void shouldVerifiesIfUserExistsWhenExistsByUsername() {
        String username = this.userRepository.save(this.funwiz)
                .getUsername();
        boolean exists = this.userRepository.existsByName(username);

        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Should find a user list when finding all")
    void shouldFindUserPageWhenFindingAll() {
        Pageable pageableMock = Mockito.mock(Pageable.class);

        int size = this.userRepository
                .saveAll(Arrays.asList(this.funwiz, this.shande, this.jk))
                .size();
        List<User> userList = this.userRepository.findAll(pageableMock)
                .getContent();

        Assertions.assertEquals(size, userList.size());
    }

    @Test
    @DisplayName("Should find user by id when find one")
    void shouldFindUserByIdWhenFindOne() {
        Long userId = this.userRepository.save(this.funwiz)
                .getUserId();
        User userFound = this.userRepository.findById(userId)
                .orElseThrow();

        Assertions.assertEquals(userId, userFound.getUserId());
    }

    @Test
    @DisplayName("Should save a user when saved")
    void shouldSaveUserWhenSaved() {
        User savedUser = this.userRepository.save(this.funwiz);
        User userFound = this.userRepository.findById(savedUser.getUserId())
                .orElseThrow();

        Assertions.assertEquals(savedUser, userFound);
    }

    @Test
    @DisplayName("Should update a user when updated")
    void shouldUpdateUserWhenUpdated() {
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";

        Long userId = this.userRepository.save(this.funwiz)
                .getUserId();
        User userToUpdate = this.userRepository.findById(userId)
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
        User savedUser = this.userRepository.save(this.funwiz);
        this.userRepository.delete(savedUser);
        boolean exists = this.userRepository.existsById(savedUser.getUserId());

        Assertions.assertFalse(exists);
    }

}