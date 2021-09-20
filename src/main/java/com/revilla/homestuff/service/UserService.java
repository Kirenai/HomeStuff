package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;

/**
 * UserService
 * @author Kirenai
 */
public interface UserService extends GeneralService<UserDto, Long> {

    ApiResponseDto register(RegisterRequestDto requestDto);

    UserDto create(UserDto data);

    ApiResponseDto update(Long id, UserDto data, AuthUserDetails userDetails);

}
