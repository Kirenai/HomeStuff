package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User userOne;
    private User userTwo;
    private User userThree;

    @BeforeEach
    void setUp() {
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
}