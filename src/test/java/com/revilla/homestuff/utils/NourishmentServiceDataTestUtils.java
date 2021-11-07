package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.AmountNourishmentDto;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Nourishment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils.getAmountNourishmentDtoMock;
import static com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils.getAmountNourishmentMock;

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

    public static Page<Nourishment> getNourishmentPage() {
        Byte unit = 1;
        return new PageImpl<>(List.of(
                getNourishmentMock(1L, "name1", "path1", "description1", getAmountNourishmentMock(1L, unit)),
                getNourishmentMock(2L, "name2", "path2", "description2", getAmountNourishmentMock(2L, unit)),
                getNourishmentMock(3L, "name3", "path3", "description3", getAmountNourishmentMock(3L, unit))
        ));
    }

    public static List<NourishmentDto> getNourishmentDtoList() {
        Byte unit = 1;
        return List.of(
                getNourishmentDtoMock(1L, "name1", "path1", "description1", getAmountNourishmentDtoMock(unit)),
                getNourishmentDtoMock(2L, "name2", "path2", "description2", getAmountNourishmentDtoMock(unit)),
                getNourishmentDtoMock(3L, "name3", "path3", "description3", getAmountNourishmentDtoMock(unit))
        );
    }
}
