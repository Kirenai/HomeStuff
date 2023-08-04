package com.revilla.homestuff.mapper.role;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper extends GenericMapper<RoleDto, Role> {

    @Override
    @Mapping(target = "users", ignore = true)
    Role mapIn(RoleDto dto);

    @Override
    RoleDto mapOut(Role input);
}
