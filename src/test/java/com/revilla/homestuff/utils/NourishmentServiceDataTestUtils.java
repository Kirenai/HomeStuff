package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.AmountNourishmentDto;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Nourishment;

public class NourishmentServiceDataTestUtils {

    public static Nourishment getNourishmentMock(Long id,
                                                 String name,
                                                 String path,
                                                 String description,
                                                 AmountNourishment amount) {
        return Nourishment.builder()
                .nourishmentId(id)
                .name(name)
                .imagePath(path)
                .description(description)
                .isAvailable(true)
                .amountNourishment(amount)
                .build();
    }

    public static NourishmentDto getNourishmentDtoMock(Long id,
                                                       String name,
                                                       String path,
                                                       String description,
                                                       AmountNourishmentDto amount) {
        return NourishmentDto.builder()
                .nourishmentId(id)
                .name(name)
                .imagePath(path)
                .description(description)
                .isAvailable(true)
                .amountNourishment(amount)
                .build();
    }

}
