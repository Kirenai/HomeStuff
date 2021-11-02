package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.AmountNourishmentDto;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.NourishmentService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.CategoryServiceDataTestUtils;
import com.revilla.homestuff.utils.NourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class NourishmentServiceImpTest {

    @Autowired
    private NourishmentService nourishmentService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private NourishmentRepository nourishmentRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ModelMapper modelMapper;

    private User userOne;
    private User userTwo;
    private Nourishment nourishmentOne;
    private NourishmentDto nourishmentDtoOne;
    private Category categoryOne;

    @BeforeEach
    void setUp() {
        Long nourishmentIdOne = 1L;
        String nameOne = "Orange";
        String imagePathOne = "./assets/orange.gif";
        String descriptionOne = "Orange the best";

        Long amountNourishmentIdOne = 1L;
        Byte unitOne = 15;

        Long userIdOne = 1L;
        String usernameOne = "kirenai";
        String passwordOne = "kirenai";
        String firstNameOne = "kirenai";
        String lastNameOne = "kirenai";
        Byte ageOne = 22;

        Long userIdTwo = 2L;
        String usernameTwo = "kirenai2";
        String passwordTwo = "kirenai2";
        String firstNameTwo = "kirenai2";
        String lastNameTwo = "kirenai2";
        Byte ageTwo = 22;

        Long categoryIdOne = 1L;
        String categoryNameOne = "Fruits";

        AmountNourishment amountNourishmentOne = AmountNourishmentServiceDataTestUtils
                .getAmountNourishmentMock(amountNourishmentIdOne, unitOne);
        this.nourishmentOne = NourishmentServiceDataTestUtils
                .getNourishmentMock(nourishmentIdOne, nameOne, imagePathOne,
                        descriptionOne, amountNourishmentOne);
        AmountNourishmentDto amountNourishmentDtoOne = AmountNourishmentServiceDataTestUtils
                .getAmountNourishmentDtoMock(unitOne);
        this.nourishmentDtoOne = NourishmentServiceDataTestUtils
                .getNourishmentDtoMock(nourishmentIdOne, nameOne, imagePathOne,
                        descriptionOne, amountNourishmentDtoOne);

        this.userOne = UserServiceDataTestUtils.getMockUser(userIdOne, usernameOne,
                passwordOne, firstNameOne, lastNameOne, ageOne);
        this.userTwo = UserServiceDataTestUtils.getMockUser(userIdTwo, usernameTwo,
                passwordTwo, firstNameTwo, lastNameTwo, ageTwo);
        this.categoryOne = CategoryServiceDataTestUtils.getCategoryMock(categoryIdOne,
                categoryNameOne);

    }

    @Test
    @DisplayName("Should throw an exception when nourishment name is already exists")
    void shouldThrowExceptionWhenNourishmentNameIsAlreadyExists() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(Nourishment.class)
                + " is already exists with name: " + this.nourishmentDtoOne.getName();
        Mockito.when(this.nourishmentRepository.existsByName(Mockito.anyString()))
                .thenReturn(true);

        EntityDuplicateConstraintViolationException ex = Assertions
                .assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.nourishmentService.create(userIdToFind,
                                categoryIdToFind, this.nourishmentDtoOne, null)
                );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
    }

    @Test
    @DisplayName("Should throw an exception when user entity is not found")
    void shouldThrowExceptionWhenUserEntityIsNotFound() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " don't found with id: " + userIdToFind;

        Mockito.when(this.nourishmentRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.create(userIdToFind, categoryIdToFind,
                        this.nourishmentDtoOne, null)
        );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        Mockito.verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when the user is unauthorized")
    void shouldThrowExceptionWhenUserIsUnauthorized() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.CREATE.name() + " this nourishment";

        Mockito.when(this.nourishmentRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(this.modelMapper.map(this.nourishmentDtoOne, Nourishment.class))
                .thenReturn(this.nourishmentOne);
        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.userOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userTwo);

        UnauthorizedPermissionException ex = Assertions.assertThrows(UnauthorizedPermissionException.class,
                () -> this.nourishmentService.create(userIdToFind, categoryIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        Mockito.verify(this.modelMapper).map(this.nourishmentDtoOne, Nourishment.class);
        Mockito.verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when category is not found")
    void shouldThrowExceptionWhenCategoryIsNotFound() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(Category.class)
                + " don't found with id: " + categoryIdToFind;

        Mockito.when(this.nourishmentRepository.existsByName(ArgumentMatchers.anyString()))
                .thenReturn(false);
        Mockito.when(this.modelMapper.map(this.nourishmentDtoOne, Nourishment.class))
                .thenReturn(this.nourishmentOne);
        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.userOne));
        Mockito.when(this.categoryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.create(userIdToFind, categoryIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        Mockito.verify(this.modelMapper).map(this.nourishmentDtoOne, Nourishment.class);
        Mockito.verify(this.userRepository).findById(userIdToFind);
        Mockito.verify(this.categoryRepository).findById(categoryIdToFind);
    }

    @Test
    @DisplayName("Should create a nourishment when everything is alright")
    void shouldCreateNourishmentWhenEverythingAlright() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expectedMessage = GeneralUtil.simpleNameClass(Nourishment.class)
                + " created successfully";

        Mockito.when(this.nourishmentRepository.existsByName(ArgumentMatchers.anyString()))
                .thenReturn(false);
        Mockito.when(this.modelMapper.map(this.nourishmentDtoOne, Nourishment.class))
                .thenReturn(this.nourishmentOne);
        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.userOne));
        Mockito.when(this.categoryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(this.categoryOne));
        Mockito.when(this.nourishmentRepository.save(ArgumentMatchers.any(Nourishment.class)))
                .thenReturn(this.nourishmentOne);
        Mockito.when(this.modelMapper.map(this.nourishmentOne, NourishmentDto.class))
                .thenReturn(this.nourishmentDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        NourishmentDto response = this.nourishmentService.create(userIdToFind, categoryIdToFind,
                this.nourishmentDtoOne, userDetails);

        Assertions.assertEquals(expectedMessage, response.getMessage());

        Mockito.verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        Mockito.verify(this.modelMapper).map(this.nourishmentDtoOne, Nourishment.class);
        Mockito.verify(this.userRepository).findById(userIdToFind);
        Mockito.verify(this.categoryRepository).findById(categoryIdToFind);
        Mockito.verify(this.nourishmentRepository).save(this.nourishmentOne);
        Mockito.verify(this.modelMapper).map(this.nourishmentOne, NourishmentDto.class);
    }

}