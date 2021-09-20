package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;

/**
 * UserService
 * @author Kirenai
 */
public interface RoleService extends GeneralService<RoleDto, Long> {

    RoleDto create(RoleDto data);

    ApiResponseDto update(Long id, RoleDto data);

}
