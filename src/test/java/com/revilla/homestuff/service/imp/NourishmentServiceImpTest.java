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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NourishmentServiceImpTest {

    @InjectMocks
    private NourishmentServiceImp nourishmentService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NourishmentRepository nourishmentRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
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

        this.userOne = UserServiceDataTestUtils.getUserMock(userIdOne, usernameOne,
                passwordOne, firstNameOne, lastNameOne, ageOne);
        this.userTwo = UserServiceDataTestUtils.getUserMock(userIdTwo, usernameTwo,
                passwordTwo, firstNameTwo, lastNameTwo, ageTwo);
        this.categoryOne = CategoryServiceDataTestUtils.getCategoryMock(categoryIdOne,
                categoryNameOne);

    }

    @Test
    @DisplayName("Should find a list nourishment we call find all")
    void shouldFindAllNourishment() {
        int expectedSize = 3;
        Pageable pageableMock = mock(Pageable.class);
        Page<Nourishment> nourishmentPageMock = NourishmentServiceDataTestUtils
                .getNourishmentPage();
        List<NourishmentDto> nourishmentDtoListMock = NourishmentServiceDataTestUtils
                .getNourishmentDtoList();

        when(this.nourishmentRepository.findAll(any(Pageable.class)))
                .thenReturn(nourishmentPageMock);
        when(this.modelMapper.map(any(Nourishment.class), eq(NourishmentDto.class)))
                .thenReturn(nourishmentDtoListMock.get(0), nourishmentDtoListMock.get(1), nourishmentDtoListMock.get(2));

        List<NourishmentDto> response = nourishmentService.findAll(pageableMock);

        assertEquals(expectedSize, response.size());
        assertEquals(nourishmentDtoListMock, response);

        verify(nourishmentRepository, times(1)).findAll(pageableMock);
        verify(modelMapper, times(3)).map(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when nourishment isn't found by id when finding one")
    void shouldThrowExceptionWhenNourishmentNotFoundByIdWhenFindingOne() {
        Long nourishmentIdToFind = 1L;
        String expected = "Nourishment not found with id: " + nourishmentIdToFind;

        when(nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                Assertions.assertThrows(EntityNoSuchElementException.class,
                        () -> nourishmentService.findOne(nourishmentIdToFind, null)
                );

        assertEquals(expected, ex.getMessage());

        verify(nourishmentRepository, times(1)).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should find a nourishment when it exists by id when find one")
    void shouldFindNourishmentWhenExistsByIdWhenFindOne() {
        Long nourishmentIdToFind = 1L;

        this.nourishmentOne.setUser(this.userOne);
        when(nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));
        when(modelMapper.map(any(), any()))
                .thenReturn(this.nourishmentDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        NourishmentDto result = nourishmentService.findOne(nourishmentIdToFind, userDetails);

        assertEquals(this.nourishmentDtoOne, result);

        verify(nourishmentRepository, times(1)).findById(nourishmentIdToFind);
        verify(modelMapper, times(1)).map(this.nourishmentOne, NourishmentDto.class);
    }

    @Test
    @DisplayName("Should find all nourishments by status when find all by status when isAvailable is true")
    void shouldFindAllNourishmentsByStatusWhenFindAllByStatusWhenIsAvailableIsTrue() {
        when(this.nourishmentRepository.findByIsAvailable(anyBoolean()))
                .thenReturn(List.of(this.nourishmentOne));
        when(this.modelMapper.map(any(), any()))
                .thenReturn(this.nourishmentDtoOne);

        List<NourishmentDto> allNourishmentByStatus = this.nourishmentService.findAllNourishmentByStatus(true);

        Assertions.assertTrue(allNourishmentByStatus.get(0).getIsAvailable());

        verify(this.nourishmentRepository, times(1)).findByIsAvailable(true);
        verify(this.modelMapper, times(1)).map(this.nourishmentOne, NourishmentDto.class);
    }

    @Test
    @DisplayName("Should find all nourishments by status when find all by status when isAvailable is false")
    void shouldFindAllNourishmentsByStatusWhenFindAllByStatusWhenIsAvailableIsFalse() {
        this.nourishmentOne.setIsAvailable(false);
        this.nourishmentDtoOne.setIsAvailable(false);
        when(this.nourishmentRepository.findByIsAvailable(anyBoolean()))
                .thenReturn(List.of(this.nourishmentOne));
        when(this.modelMapper.map(any(), any()))
                .thenReturn(this.nourishmentDtoOne);

        List<NourishmentDto> allNourishmentByStatus = this.nourishmentService.findAllNourishmentByStatus(false);

        Assertions.assertFalse(allNourishmentByStatus.get(0).getIsAvailable());

        verify(this.nourishmentRepository, times(1)).findByIsAvailable(false);
        verify(this.modelMapper, times(1)).map(this.nourishmentOne, NourishmentDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when nourishment name is already exists when creating")
    void shouldThrowExceptionWhenNourishmentNameIsAlreadyExists() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(Nourishment.class)
                + " is already exists with name: " + this.nourishmentDtoOne.getName();
        when(this.nourishmentRepository.existsByName(Mockito.anyString()))
                .thenReturn(true);

        EntityDuplicateConstraintViolationException ex = Assertions
                .assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.nourishmentService.create(userIdToFind,
                                categoryIdToFind, this.nourishmentDtoOne, null)
                );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
    }

    @Test
    @DisplayName("Should throw an exception when user entity is not found when creating")
    void shouldThrowExceptionWhenUserEntityIsNotFound() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = "User not found with id: " + userIdToFind;

        when(this.nourishmentRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);
        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.create(userIdToFind, categoryIdToFind,
                        this.nourishmentDtoOne, null)
        );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when the user is unauthorized when creating")
    void shouldThrowExceptionWhenUserIsUnauthorized() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.CREATE.name() + " this nourishment";

        when(this.nourishmentRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);
        when(this.modelMapper.map(this.nourishmentDtoOne, Nourishment.class))
                .thenReturn(this.nourishmentOne);
        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userTwo);

        UnauthorizedPermissionException ex = Assertions.assertThrows(UnauthorizedPermissionException.class,
                () -> this.nourishmentService.create(userIdToFind, categoryIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        verify(this.modelMapper).map(this.nourishmentDtoOne, Nourishment.class);
        verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when category is not found when creating")
    void shouldThrowExceptionWhenCategoryIsNotFound() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = "Category not found with id: " + categoryIdToFind;

        when(this.nourishmentRepository.existsByName(anyString()))
                .thenReturn(false);
        when(this.modelMapper.map(this.nourishmentDtoOne, Nourishment.class))
                .thenReturn(this.nourishmentOne);
        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userOne));
        when(this.categoryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.create(userIdToFind, categoryIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        verify(this.modelMapper).map(this.nourishmentDtoOne, Nourishment.class);
        verify(this.userRepository).findById(userIdToFind);
        verify(this.categoryRepository).findById(categoryIdToFind);
    }

    @Test
    @DisplayName("Should create a nourishment when everything is alright when creating")
    void shouldCreateNourishmentWhenEverythingAlright() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expectedMessage = GeneralUtil.simpleNameClass(Nourishment.class)
                + " created successfully";

        when(this.nourishmentRepository.existsByName(anyString()))
                .thenReturn(false);
        when(this.modelMapper.map(this.nourishmentDtoOne, Nourishment.class))
                .thenReturn(this.nourishmentOne);
        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userOne));
        when(this.categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.categoryOne));
        when(this.nourishmentRepository.save(any(Nourishment.class)))
                .thenReturn(this.nourishmentOne);
        when(this.modelMapper.map(this.nourishmentOne, NourishmentDto.class))
                .thenReturn(this.nourishmentDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        NourishmentDto response = this.nourishmentService.create(userIdToFind, categoryIdToFind,
                this.nourishmentDtoOne, userDetails);

        assertEquals(expectedMessage, response.getMessage());

        verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
        verify(this.modelMapper).map(this.nourishmentDtoOne, Nourishment.class);
        verify(this.userRepository).findById(userIdToFind);
        verify(this.categoryRepository).findById(categoryIdToFind);
        verify(this.nourishmentRepository).save(this.nourishmentOne);
        verify(this.modelMapper).map(this.nourishmentOne, NourishmentDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when nourishment is not found when updating")
    void shouldThrowExceptionWhenNourishmentNotFoundWhenUpdating() {
        Long nourishmentIdToFind = 1L;
        String expected = "Nourishment not found with id: " + nourishmentIdToFind;

        when(this.nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.update(nourishmentIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized  when updating")
    void shouldThrowExceptionWhenUserUnauthenticatedWhenUpdating() {
        Long nourishmentIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.UPDATE.name() + " this nourishment";

        this.nourishmentOne.setUser(this.userTwo);
        when(this.nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        UnauthorizedPermissionException ex = Assertions.assertThrows(UnauthorizedPermissionException.class,
                () -> this.nourishmentService.update(nourishmentIdToFind,
                        this.nourishmentDtoOne, userDetails)
        );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should update a nourishment when updating")
    void shouldUpdateNourishment() {
        Long nourishmentIdToFind = 1L;
        String expectedMessage = GeneralUtil.simpleNameClass(Nourishment.class)
                + " updated successfully";

        this.nourishmentOne.setUser(this.userOne);
        when(this.nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ApiResponseDto response = this.nourishmentService.update(nourishmentIdToFind,
                this.nourishmentDtoOne, userDetails);

        assertEquals(expectedMessage, response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when nourishment is not found when deleting")
    void shouldThrowExceptionWhenNourishmentNotFoundWhenDeleting() {
        Long nourishmentIdToFind = 1L;
        String expected = "Nourishment not found with id: " + nourishmentIdToFind;

        when(this.nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> this.nourishmentService.delete(nourishmentIdToFind, userDetails)
        );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized  when deleting")
    void shouldThrowExceptionWhenUserUnauthenticatedWhenDeleting() {
        Long nourishmentIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.DELETE.name() + " this nourishment";

        this.nourishmentOne.setUser(this.userTwo);
        when(this.nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        UnauthorizedPermissionException ex = Assertions.assertThrows(UnauthorizedPermissionException.class,
                () -> this.nourishmentService.delete(nourishmentIdToFind, userDetails)
        );

        assertEquals(expected, ex.getMessage());

        verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should delete a nourishment when deleting")
    void shouldDeleteNourishment() {
        Long nourishmentIdToFind = 1L;
        String expectedMessage = GeneralUtil.simpleNameClass(Nourishment.class)
                + " deleted successfully";

        this.nourishmentOne.setUser(this.userOne);
        when(this.nourishmentRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.nourishmentOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ApiResponseDto response = this.nourishmentService.delete(nourishmentIdToFind, userDetails);

        assertEquals(expectedMessage, response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        verify(this.nourishmentRepository).findById(nourishmentIdToFind);
    }

}