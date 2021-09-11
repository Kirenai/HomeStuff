package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.security.AuthUserDetails;

/**
 * UserService
 * @author Kirenai
 */
public interface UserService extends GeneralService<UserDto, Long> {
    
    UserDto create(UserDto data);

    UserDto update(Long id, UserDto data, AuthUserDetails userDetails);

}
