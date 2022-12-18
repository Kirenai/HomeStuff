package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.utils.CategoryServiceDataTestUtils;
import com.revilla.homestuff.utils.NourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class NourishmentRepositoryTest {

    private final NourishmentRepository nourishmentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private Nourishment apple;
    private Nourishment banana;

    private User funwiz;

    private Category fruit;

    @Autowired
    NourishmentRepositoryTest(NourishmentRepository nourishmentRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository) {
        this.nourishmentRepository = nourishmentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        String usernameOne = "funwiz";
        String passwordOne = "funwiz123";
        String firstNameOne = "funwiz";
        String lastNameOne = "funwiz";
        Byte age = 20;

        String apple = "Apple";
        String banana = "Banana";
        String fruit = "Fruit";

        this.apple = NourishmentServiceDataTestUtils.getNourishmentMock(apple);
        this.banana = NourishmentServiceDataTestUtils.getNourishmentMock(banana);

        this.funwiz = UserServiceDataTestUtils.getUserMock(usernameOne,
                passwordOne, firstNameOne, lastNameOne, age);

        this.fruit = CategoryServiceDataTestUtils.getCategoryMock(fruit);
    }

    @Test
    @DisplayName("should return true when a nourishment exits by name")
    void shouldReturnTrueWhenANourishmentExitsByName() {
        User funwiz = this.userRepository.save(this.funwiz);
        Category fruit = this.categoryRepository.save(this.fruit);
        this.apple.setUser(funwiz);
        this.apple.setCategory(fruit);
        String name = this.nourishmentRepository.save(this.apple)
                .getName();

        assertTrue(this.nourishmentRepository.existsByName(name));
    }

    @Test
    @DisplayName("should return false when a nourishment does not exist by name")
    void shouldReturnFalseWhenANourishmentDoesNotExistByName() {
        User funwiz = this.userRepository.save(this.funwiz);
        Category fruit = this.categoryRepository.save(this.fruit);
        this.apple.setUser(funwiz);
        this.apple.setCategory(fruit);
        this.nourishmentRepository.save(this.apple);

        assertFalse(this.nourishmentRepository.existsByName(this.banana.getName()));
    }

    @Test
    @DisplayName("should return a list of nourishments when isAvailable is true")
    void shouldReturnAListOfNourishmentsWhenIsAvailableIsTrue() {
        User funwiz = this.userRepository.save(this.funwiz);
        Category fruit = this.categoryRepository.save(this.fruit);
        this.apple.setUser(funwiz);
        this.apple.setCategory(fruit);
        this.nourishmentRepository.save(this.apple); // isAvailable = true

        List<Nourishment> nourishmentList = this.nourishmentRepository.findByIsAvailable(true);
        assertEquals(1, nourishmentList.size());
        assertTrue(nourishmentList.get(0).getIsAvailable());

    }

    @Test
    @DisplayName("should return a empty list of nourishments when isAvailable is false")
    void shouldReturnAEmptyListOfNourishmentsWhenIsAvailableIsFalse() {
        User funwiz = this.userRepository.save(this.funwiz);
        Category fruit = this.categoryRepository.save(this.fruit);
        this.apple.setUser(funwiz);
        this.apple.setCategory(fruit);
        this.nourishmentRepository.save(this.apple); // isAvailable = true

        assertEquals(0, this.nourishmentRepository.findByIsAvailable(false).size());
    }

}