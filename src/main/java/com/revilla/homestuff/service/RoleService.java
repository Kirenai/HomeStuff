package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.RoleDto;

/**
 * UserService
 * @author Kirenai
 */
public interface RoleService extends GeneralService<RoleDto, Long> {

    RoleDto create(RoleDto data);

    RoleDto update(Long id, RoleDto data);

}
