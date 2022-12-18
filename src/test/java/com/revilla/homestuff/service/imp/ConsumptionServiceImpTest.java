package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Consumption;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.ConsumptionRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.ConsumptionServiceDataTestUtils;
import com.revilla.homestuff.utils.NourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumptionServiceImpTest {

    @InjectMocks
    private ConsumptionServiceImp consumptionService;
    @Mock
    private ConsumptionRepository consumptionRepository;
    @Mock
    private NourishmentRepository nourishmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    private User userOne;
    private User userTwo;
    private Nourishment nourishmentOne;
    private Consumption consumptionOne;
    private Consumption consumptionTwo;
    private ConsumptionDto consumptionDtoOne;
    private ConsumptionDto consumptionDtoTwo;

    @BeforeEach
    void setUp() {
        Long userIdOne = 1L;
        Long userIdTwo = 2L;
        String usernameOne = "kirenai";
        String passwordOne = "kirenai";
        String firstNameOne = "kirenai";
        String lastNameOne = "kirenai";
        Byte ageOne = 22;

        Long nourishmentIdOne = 1L;
        String nameOne = "Orange";
        String imagePathOne = "./assets/orange.gif";
        String descriptionOne = "Orange the best";

        Long amountNourishmentIdOne = 1L;
        Byte unitOne = 15;

        Byte unitConsumptionOne = 10;
        Byte unitConsumptionTwo = 5;

        this.userOne = UserServiceDataTestUtils.getUserMock(userIdOne,
                usernameOne, passwordOne, firstNameOne, lastNameOne, ageOne);
        this.userTwo = UserServiceDataTestUtils.getUserMock(userIdTwo,
                usernameOne, passwordOne, firstNameOne, lastNameOne, ageOne);
        AmountNourishment amountNourishmentMock = AmountNourishmentServiceDataTestUtils
                .getAmountNourishmentMock(amountNourishmentIdOne, unitOne);
        this.nourishmentOne = NourishmentServiceDataTestUtils
                .getNourishmentMock(nourishmentIdOne, nameOne, imagePathOne,
                        descriptionOne, amountNourishmentMock);
        this.consumptionOne = ConsumptionServiceDataTestUtils
                .getConsumptionMock(unitConsumptionOne);
        this.consumptionTwo = ConsumptionServiceDataTestUtils
                .getConsumptionMock(unitConsumptionTwo);
        this.consumptionDtoOne = ConsumptionServiceDataTestUtils
                .getConsumptionDtoMock(unitConsumptionOne);
        this.consumptionDtoTwo = ConsumptionServiceDataTestUtils
                .getConsumptionDtoMock(unitConsumptionTwo);
    }

    @Test
    @DisplayName("Should find a consumption list when finding all")
    void shouldFindConsumptionListWhenFindingAll() {
        int expectedSize = 2;
        Pageable pageableMock = Mockito.mock(Pageable.class);
        when(this.consumptionRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(this.consumptionOne, this.consumptionTwo)));
        when(this.modelMapper.map(any(Consumption.class), eq(ConsumptionDto.class)))
                .thenReturn(this.consumptionDtoOne, this.consumptionDtoTwo);

        List<ConsumptionDto> response = consumptionService.findAll(pageableMock);

        assertEquals(expectedSize, response.size());
        assertEquals(List.of(this.consumptionDtoOne, this.consumptionDtoTwo), response);

        verify(consumptionRepository, Mockito.times(1))
                .findAll(pageableMock);
        verify(modelMapper, Mockito.times(2))
                .map(any(Consumption.class), eq(ConsumptionDto.class));
    }

    @Test
    @DisplayName("Should throw an exception when user is not found by id when finding one")
    void shouldThrowExceptionWhenUserIsNotFoundByIdWhenFindingOne() {
        Long consumptionIdToFind = 1L;
        String messageExpected = "Consumption not found with id: "
                + consumptionIdToFind;

        when(this.consumptionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> consumptionService.findOne(consumptionIdToFind, null));

        assertEquals(messageExpected, ex.getMessage());

        verify(this.consumptionRepository, Mockito.times(1))
                .findById(consumptionIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user is not authorized when finding one")
    void shouldThrowExceptionWhenUserIsNotAuthorizedWhenFindingOne() {
        Long consumptionIdToFind = 1L;
        String messageExpected = "You don't have the permission to "
                + MessageAction.ACCESS.name() + " this consumption";

        this.consumptionOne.setUser(this.userOne);
        when(this.consumptionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.consumptionOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userTwo);

        UnauthorizedPermissionException ex = assertThrows(UnauthorizedPermissionException.class,
                () -> consumptionService.findOne(consumptionIdToFind, userDetails));

        assertEquals(messageExpected, ex.getMessage());

        verify(this.consumptionRepository, Mockito.times(1))
                .findById(consumptionIdToFind);
    }

    @Test
    @DisplayName("Should find a consumption when finding one")
    void shouldFindConsumptionWhenFindingOne() {
        Long consumptionIdToFind = 1L;

        this.consumptionOne.setUser(this.userOne);
        when(this.consumptionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.consumptionOne));
        when(modelMapper.map(any(Consumption.class), eq(ConsumptionDto.class)))
                .thenReturn(this.consumptionDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ConsumptionDto response = consumptionService.findOne(consumptionIdToFind, userDetails);

        assertEquals(this.consumptionDtoOne, response);

        verify(this.consumptionRepository, Mockito.times(1))
                .findById(consumptionIdToFind);
        verify(modelMapper, Mockito.times(1))
                .map(this.consumptionOne, ConsumptionDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when user not found by id when creating")
    void shouldThrowExceptionWhenUserNotFoundByIdWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        String exceptionMessage = "User not found with id: " + userIdToFind;

        when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> consumptionService.create(nourishmentIdToFind, userIdToFind,
                        new ConsumptionDto(), null));

        assertEquals(exceptionMessage, ex.getMessage());

        verify(userRepository, Mockito.times(1)).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when nourishment not found by id when creating")
    void shouldThrowExceptionWhenNourishmentNotFoundByIdWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        String exceptionMessage = "Nourishment not found with id: " + nourishmentIdToFind;

        when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.of(userOne));
        when(nourishmentRepository.findById(nourishmentIdToFind))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> consumptionService.create(nourishmentIdToFind, userIdToFind,
                        new ConsumptionDto(), null));

        assertEquals(exceptionMessage, ex.getMessage());

        verify(userRepository, Mockito.times(1)).findById(userIdToFind);
        verify(nourishmentRepository, Mockito.times(1)).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user has no authorization when creating")
    void shouldThrowExceptionWhenUserHasNoAuthorizationWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        String exceptionMessage = "You don't have the permission to "
                + MessageAction.CREATE.name() + " this consumption";

        when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.of(userOne));
        when(nourishmentRepository.findById(nourishmentIdToFind))
                .thenReturn(Optional.of(nourishmentOne));
        when(modelMapper.map(any(), eq(Consumption.class)))
                .thenReturn(this.consumptionOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userTwo);

        UnauthorizedPermissionException ex =
                assertThrows(UnauthorizedPermissionException.class,
                        () -> consumptionService.create(nourishmentIdToFind, userIdToFind,
                                this.consumptionDtoOne, userDetails));

        assertEquals(exceptionMessage, ex.getMessage());

        verify(userRepository, Mockito.times(1))
                .findById(userIdToFind);
        verify(nourishmentRepository, Mockito.times(1))
                .findById(nourishmentIdToFind);
        verify(modelMapper, Mockito.times(1))
                .map(this.consumptionDtoOne, Consumption.class);
    }

    @Test
    @DisplayName("Should create a consumption when user has authorization when creating")
    void shouldCreateConsumptionWhenUserHasAuthorizationWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.of(userOne));
        when(nourishmentRepository.findById(nourishmentIdToFind))
                .thenReturn(Optional.of(nourishmentOne));
        when(modelMapper.map(any(), eq(Consumption.class)))
                .thenReturn(this.consumptionOne);
        when(consumptionRepository.save(any()))
                .thenReturn(this.consumptionOne);
        when(modelMapper.map(any(), eq(ConsumptionDto.class)))
                .thenReturn(this.consumptionDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ConsumptionDto consumptionCreated = consumptionService.create(nourishmentIdToFind,
                userIdToFind, this.consumptionDtoOne, userDetails);

        assertEquals(this.consumptionDtoOne, consumptionCreated);

        verify(userRepository, Mockito.times(1))
                .findById(userIdToFind);
        verify(nourishmentRepository, Mockito.times(1))
                .findById(nourishmentIdToFind);
        verify(modelMapper, Mockito.times(1))
                .map(this.consumptionDtoOne, Consumption.class);
        verify(consumptionRepository, Mockito.times(1))
                .save(this.consumptionOne);
        verify(modelMapper, Mockito.times(1))
                .map(this.consumptionOne, ConsumptionDto.class);
    }

}