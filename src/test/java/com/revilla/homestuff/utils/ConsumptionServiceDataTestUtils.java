package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.Consumption;

import java.util.List;

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

    public static ConsumptionDto getConsumptionDtoMock(Long consuptionId, Byte unit) {
        return ConsumptionDto.builder()
                .consumptionId(consuptionId)
                .unit(unit)
                .build();
    }

    public static List<ConsumptionDto> getConsumptionDtoListMock() {
        return List.of(
                getConsumptionDtoMock(1L, (byte) 5),
                getConsumptionDtoMock(2L, (byte) 10),
                getConsumptionDtoMock(3L, (byte) 15)
        );
    }

}
