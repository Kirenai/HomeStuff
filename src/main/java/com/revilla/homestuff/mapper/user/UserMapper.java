package com.revilla.homestuff.mapper.user;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserDto, User> {

    @Override
    @Mapping(target = "nourishments", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "consumptions", ignore = true)
    User mapIn(UserDto dto);

    @Override
    UserDto mapOut(User input);

}
