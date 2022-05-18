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
import com.revilla.homestuff.service.ConsumptionService;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.ConsumptionServiceDataTestUtils;
import com.revilla.homestuff.utils.NourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class ConsumptionServiceImpTest {

    @Autowired
    private ConsumptionService consumptionService;
    @MockBean
    private ConsumptionRepository consumptionRepository;
    @MockBean
    private NourishmentRepository nourishmentRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
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

        this.userOne = UserServiceDataTestUtils.getMockUser(userIdOne,
                usernameOne, passwordOne, firstNameOne, lastNameOne, ageOne);
        this.userTwo = UserServiceDataTestUtils.getMockUser(userIdTwo,
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
        Mockito.when(consumptionRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(this.consumptionOne, this.consumptionTwo)));
        Mockito.when(modelMapper.map(ArgumentMatchers.any(Consumption.class), ArgumentMatchers.eq(ConsumptionDto.class)))
                .thenReturn(this.consumptionDtoOne, this.consumptionDtoTwo);

        List<ConsumptionDto> response = consumptionService.findAll(pageableMock);

        assertEquals(expectedSize, response.size());
        assertEquals(List.of(this.consumptionDtoOne, this.consumptionDtoTwo), response);

        Mockito.verify(consumptionRepository, Mockito.times(1))
                .findAll(pageableMock);
        Mockito.verify(modelMapper, Mockito.times(2))
                .map(ArgumentMatchers.any(Consumption.class), ArgumentMatchers.eq(ConsumptionDto.class));
    }

    @Test
    @DisplayName("Should throw an exception when user is not found by id when finding one")
    void shouldThrowExceptionWhenUserIsNotFoundByIdWhenFindingOne() {
        Long consumptionIdToFind = 1L;
        String messageExpected = "Consumption not found with id: "
                + consumptionIdToFind;

        Mockito.when(this.consumptionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> consumptionService.findOne(consumptionIdToFind, null));

        assertEquals(messageExpected, ex.getMessage());

        Mockito.verify(this.consumptionRepository, Mockito.times(1))
                .findById(consumptionIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user is not authorized when finding one")
    void shouldThrowExceptionWhenUserIsNotAuthorizedWhenFindingOne() {
        Long consumptionIdToFind = 1L;
        String messageExpected = "You don't have the permission to "
                + MessageAction.ACCESS.name() + " this consumption";

        this.consumptionOne.setUser(this.userOne);
        Mockito.when(this.consumptionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.consumptionOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userTwo);

        UnauthorizedPermissionException ex = assertThrows(UnauthorizedPermissionException.class,
                () -> consumptionService.findOne(consumptionIdToFind, userDetails));

        assertEquals(messageExpected, ex.getMessage());

        Mockito.verify(this.consumptionRepository, Mockito.times(1))
                .findById(consumptionIdToFind);
    }

    @Test
    @DisplayName("Should find a consumption when finding one")
    void shouldFindConsumptionWhenFindingOne() {
        Long consumptionIdToFind = 1L;

        this.consumptionOne.setUser(this.userOne);
        Mockito.when(this.consumptionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.consumptionOne));
        Mockito.when(modelMapper.map(ArgumentMatchers.any(Consumption.class), ArgumentMatchers.eq(ConsumptionDto.class)))
                .thenReturn(this.consumptionDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ConsumptionDto response = consumptionService.findOne(consumptionIdToFind, userDetails);

        assertEquals(this.consumptionDtoOne, response);

        Mockito.verify(this.consumptionRepository, Mockito.times(1))
                .findById(consumptionIdToFind);
        Mockito.verify(modelMapper, Mockito.times(1))
                .map(this.consumptionOne, ConsumptionDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when user not found by id when creating")
    void shouldThrowExceptionWhenUserNotFoundByIdWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        String exceptionMessage = "User not found with id: " + userIdToFind;

        Mockito.when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> consumptionService.create(nourishmentIdToFind, userIdToFind,
                        new ConsumptionDto(), null));

        assertEquals(exceptionMessage, ex.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when nourishment not found by id when creating")
    void shouldThrowExceptionWhenNourishmentNotFoundByIdWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        String exceptionMessage = "Nourishment not found with id: " + nourishmentIdToFind;

        Mockito.when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.of(userOne));
        Mockito.when(nourishmentRepository.findById(nourishmentIdToFind))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> consumptionService.create(nourishmentIdToFind, userIdToFind,
                        new ConsumptionDto(), null));

        assertEquals(exceptionMessage, ex.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userIdToFind);
        Mockito.verify(nourishmentRepository, Mockito.times(1)).findById(nourishmentIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user has no authorization when creating")
    void shouldThrowExceptionWhenUserHasNoAuthorizationWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        String exceptionMessage = "You don't have the permission to "
                + MessageAction.CREATE.name() + " this consumption";

        Mockito.when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.of(userOne));
        Mockito.when(nourishmentRepository.findById(nourishmentIdToFind))
                .thenReturn(Optional.of(nourishmentOne));
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(Consumption.class)))
                .thenReturn(this.consumptionOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userTwo);

        UnauthorizedPermissionException ex =
                assertThrows(UnauthorizedPermissionException.class,
                        () -> consumptionService.create(nourishmentIdToFind, userIdToFind,
                                this.consumptionDtoOne, userDetails));

        assertEquals(exceptionMessage, ex.getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(userIdToFind);
        Mockito.verify(nourishmentRepository, Mockito.times(1))
                .findById(nourishmentIdToFind);
        Mockito.verify(modelMapper, Mockito.times(1))
                .map(this.consumptionDtoOne, Consumption.class);
    }

    @Test
    @DisplayName("Should create a consumption when user has authorization when creating")
    void shouldCreateConsumptionWhenUserHasAuthorizationWhenCreating() {
        Long userIdToFind = 1L;
        Long nourishmentIdToFind = 1L;
        Mockito.when(userRepository.findById(userIdToFind))
                .thenReturn(Optional.of(userOne));
        Mockito.when(nourishmentRepository.findById(nourishmentIdToFind))
                .thenReturn(Optional.of(nourishmentOne));
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(Consumption.class)))
                .thenReturn(this.consumptionOne);
        Mockito.when(consumptionRepository.save(Mockito.any()))
                .thenReturn(this.consumptionOne);
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(ConsumptionDto.class)))
                .thenReturn(this.consumptionDtoOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        ConsumptionDto consumptionCreated = consumptionService.create(nourishmentIdToFind,
                userIdToFind, this.consumptionDtoOne, userDetails);

        assertEquals(this.consumptionDtoOne, consumptionCreated);

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(userIdToFind);
        Mockito.verify(nourishmentRepository, Mockito.times(1))
                .findById(nourishmentIdToFind);
        Mockito.verify(modelMapper, Mockito.times(1))
                .map(this.consumptionDtoOne, Consumption.class);
        Mockito.verify(consumptionRepository, Mockito.times(1))
                .save(this.consumptionOne);
        Mockito.verify(modelMapper, Mockito.times(1))
                .map(this.consumptionOne, ConsumptionDto.class);
    }

}