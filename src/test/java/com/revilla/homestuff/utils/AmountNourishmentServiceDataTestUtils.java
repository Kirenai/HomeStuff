package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.AmountNourishmentDto;
import com.revilla.homestuff.entity.AmountNourishment;

import java.math.BigDecimal;

public class AmountNourishmentServiceDataTestUtils {

    public static AmountNourishment getAmountNourishmentMock(Long id, BigDecimal percentage) {
        return AmountNourishment.builder()
                .amountNourishmentId(id)
                .percentage(percentage)
                .build();
    }

    public static AmountNourishment getAmountNourishmentMock(Long id, Byte unit) {
        return AmountNourishment.builder()
                .amountNourishmentId(id)
                .unit(unit)
                .build();
    }

    public static AmountNourishmentDto getAmountNourishmentDtoMock(BigDecimal percentage) {
        return AmountNourishmentDto.builder()
                .percentage(percentage)
                .build();
    }

    public static AmountNourishmentDto getAmountNourishmentDtoMock(Byte unit) {
        return AmountNourishmentDto.builder()
                .unit(unit)
                .build();
    }

}
