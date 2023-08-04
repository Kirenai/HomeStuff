package com.revilla.homestuff.mapper.nourishment;

import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NourishmentMapper extends GenericMapper<NourishmentDto, Nourishment> {

    @Override
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "consumptions", ignore = true)
    @Mapping(target = "amountNourishment", ignore = true)
    Nourishment mapIn(NourishmentDto dto);

    @Override
    NourishmentDto mapOut(Nourishment input);
}
