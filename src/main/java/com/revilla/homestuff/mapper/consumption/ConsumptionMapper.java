package com.revilla.homestuff.mapper.consumption;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.Consumption;
import com.revilla.homestuff.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsumptionMapper extends GenericMapper<ConsumptionDto, Consumption> {

    @Override
    @Mapping(target = "user", ignore = true)
    Consumption mapIn(ConsumptionDto dto);

    @Override
    ConsumptionDto mapOut(Consumption input);

}
