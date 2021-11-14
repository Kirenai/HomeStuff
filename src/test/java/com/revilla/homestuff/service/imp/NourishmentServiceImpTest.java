package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.AmountNourishmentDto;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
    @DisplayName("Should find a list nourishment we call find all")
    void shouldFindAllNourishment() {
        int expectedSize = 3;
        Pageable pageableMock = Mockito.mock(Pageable.class);
        Page<Nourishment> nourishmentPageMock = NourishmentServiceDataTestUtils
                .getNourishmentPage();
        List<NourishmentDto> nourishmentDtoListMock = NourishmentServiceDataTestUtils
                .getNourishmentDtoList();

        Mockito.when(nourishmentRepository.findAll(pageableMock))
                .thenReturn(nourishmentPageMock);
        Mockito.when(modelMapper.map(nourishmentPageMock.getContent().get(0), NourishmentDto.class))
                .thenReturn(nourishmentDtoListMock.get(0));
        Mockito.when(modelMapper.map(nourishmentPageMock.getContent().get(1), NourishmentDto.class))
                .thenReturn(nourishmentDtoListMock.get(1));
        Mockito.when(modelMapper.map(nourishmentPageMock.getContent().get(2), NourishmentDto.class))
                .thenReturn(nourishmentDtoListMock.get(2));

        List<NourishmentDto> response = nourishmentService.findAll(pageableMock);

        Assertions.assertEquals(expectedSize, response.size());
        Assertions.assertEquals(nourishmentDtoListMock, response);

        Mockito.verify(nourishmentRepository, Mockito.times(1)).findAll(pageableMock);
        Mockito.verify(modelMapper, Mockito.times(3)).map(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should throw exception when nourishment isn't found by id when finding one")
    void shouldThrowExceptionWhenNourishmentNotFoundByIdWhenFindingOne() {
        Long nourishmentIdToFind = 1L;
        String expected = "Nourishment not found with id: " + nourishmentIdToFind;

        Mockito.when(nourishmentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                Assertions.assertThrows(EntityNoSuchElementException.class,
                        () -> nourishmentService.findOne(nourishmentIdToFind, null)
                );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(nourishmentRepository, Mockito.times(1)).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should find a nourishment when it exists by id when find one")
    void shouldFindNourishmentWhenExistsByIdWhenFindOne() {
        Long nourishmentIdToFind = 1L;

        this.nourishmentOne.setUser(this.userOne);
        Mockito.when(nourishmentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));
        Mockito.when(modelMapper.map(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(this.nourishmentDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        NourishmentDto result = nourishmentService.findOne(nourishmentIdToFind, userDetails);

        Assertions.assertEquals(this.nourishmentDtoOne, result);

        Mockito.verify(nourishmentRepository, Mockito.times(1)).findById(nourishmentIdToFind);
        Mockito.verify(modelMapper, Mockito.times(1)).map(this.nourishmentOne, NourishmentDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when nourishment name is already exists when creating")
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
    @DisplayName("Should throw an exception when user entity is not found when creating")
    void shouldThrowExceptionWhenUserEntityIsNotFound() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = "User not found with id: " + userIdToFind;

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
    @DisplayName("Should throw an exception when the user is unauthorized when creating")
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
    @DisplayName("Should throw an exception when category is not found when creating")
    void shouldThrowExceptionWhenCategoryIsNotFound() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = "Category not found with id: " + categoryIdToFind;

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
    @DisplayName("Should create a nourishment when everything is alright when creating")
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

    @Test
    @DisplayName("Should throw an exception when nourishment is not found when updating")
    void shouldThrowExceptionWhenNourishmentNotFoundWhenUpdating() {
        Long nourishmentIdToFind = 1L;
        String expected = "Nourishment not found with id: " + nourishmentIdToFind;

        Mockito.when(this.nourishmentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.update(nourishmentIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized  when updating")
    void shouldThrowExceptionWhenUserUnauthenticatedWhenUpdating() {
        Long nourishmentIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.UPDATE.name() + " this nourishment";

        this.nourishmentOne.setUser(this.userTwo);
        Mockito.when(this.nourishmentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        UnauthorizedPermissionException ex = Assertions.assertThrows(UnauthorizedPermissionException.class,
                () -> this.nourishmentService.update(nourishmentIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should update a nourishment when updating")
    void shouldUpdateNourishment() {
        Long nourishmentIdToFind = 1L;
        String expectedMessage = GeneralUtil.simpleNameClass(Nourishment.class)
                + " updated successfully";

        this.nourishmentOne.setUser(this.userOne);
        Mockito.when(this.nourishmentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));
        Mockito.when(this.nourishmentRepository.save(ArgumentMatchers.any(Nourishment.class)))
                .thenReturn(this.nourishmentOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ApiResponseDto response = this.nourishmentService.update(nourishmentIdToFind,
                this.nourishmentDtoOne, userDetails);

        Assertions.assertEquals(expectedMessage, response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        Mockito.verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when nourishment is not found when deleting")
    void shouldThrowExceptionWhenNourishmentNotFoundWhenDeleting() {
        Long nourishmentIdToFind = 1L;
        String expected = "Nourishment not found with id: " + nourishmentIdToFind;

        Mockito.when(this.nourishmentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.delete(nourishmentIdToFind, userDetails)
        );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized  when deleting")
    void shouldThrowExceptionWhenUserUnauthenticatedWhenDeleting() {
        Long nourishmentIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.DELETE.name() + " this nourishment";

        this.nourishmentOne.setUser(this.userTwo);
        Mockito.when(this.nourishmentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        UnauthorizedPermissionException ex = Assertions.assertThrows(UnauthorizedPermissionException.class,
                () -> this.nourishmentService.delete(nourishmentIdToFind, userDetails)
        );

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should delete a nourishment when deleting")
    void shouldDeleteNourishment() {
        Long nourishmentIdToFind = 1L;
        String expectedMessage = GeneralUtil.simpleNameClass(Nourishment.class)
                + " deleted successfully";

        this.nourishmentOne.setUser(this.userOne);
        Mockito.when(this.nourishmentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));
        Mockito.when(this.nourishmentRepository.save(ArgumentMatchers.any(Nourishment.class)))
                .thenReturn(this.nourishmentOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ApiResponseDto response = this.nourishmentService.delete(nourishmentIdToFind, userDetails);

        Assertions.assertEquals(expectedMessage, response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        Mockito.verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

}