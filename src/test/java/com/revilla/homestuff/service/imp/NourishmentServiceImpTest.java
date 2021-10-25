package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.AmountNourishmentDto;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.NourishmentService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.NourishmentServiceDataTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    private Nourishment nourishmentOne;
    private NourishmentDto nourishmentDtoOne;

    @BeforeEach
    void setUp() {
        Long nourishmentIdOne = 1L;
        String nameOne = "Orange";
        String imagePathOne = "./assets/orange.gif";
        String descriptionOne = "Orange the best";

        Long amountNourishmentIdOne = 1L;
        Byte unitOne = 15;

        AmountNourishment amountNourishmentOne = AmountNourishmentServiceDataTestUtils
                .getAmountNourishmentMock(amountNourishmentIdOne, unitOne);
        this.nourishmentOne = NourishmentServiceDataTestUtils
                .getNourishmentMock(nourishmentIdOne, nameOne, imagePathOne,
                        descriptionOne, amountNourishmentOne);
        AmountNourishmentDto amountNourishmentDtoOne = AmountNourishmentServiceDataTestUtils
                .getAmountNourishmentDtoMock(unitOne);
        this.nourishmentDtoOne = NourishmentServiceDataTestUtils
                .getNourishmentDtoMock(nourishmentIdOne, nameOne, imagePathOne,
                        descriptionOne,amountNourishmentDtoOne);
    }

    @Test
    @DisplayName("Should throw a exception when nourishment name is already exists")
    void shouldThrowExceptionWhenNourishmentNameIsAlreadyExists() {
        Long userIdToFind = 1L;
        Long categoryIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(Nourishment.class)
                + " is already exists with name: " + this.nourishmentDtoOne.getName();
        Mockito.when(this.nourishmentRepository.existsByName(Mockito.anyString()))
                .thenReturn(true);

        EntityDuplicateConstraintViolationException ex = Assertions.assertThrows(EntityDuplicateConstraintViolationException.class,
                () -> this.nourishmentService.create(userIdToFind, categoryIdToFind,
                        this.nourishmentDtoOne , null));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.nourishmentRepository).existsByName(this.nourishmentDtoOne.getName());
    }

}