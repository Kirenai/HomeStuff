package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.Consumption;

public class ConsumptionServiceDataTestUtils {

    public static Consumption getConsumptionMock(Byte unit) {
        return Consumption.builder()
                .unit(unit)
                .build();
    }

    public static ConsumptionDto getConsumptionDtoMock(Byte unit) {
        return ConsumptionDto.builder()
                .unit(unit)
                .build();
    }

}
