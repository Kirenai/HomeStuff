package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.util.enums.RoleName;

import java.util.Collections;
import java.util.List;

public class RoleServiceDataTestUtils {

    public static RoleDto getMockRoleDto(Long roleID, RoleName roleName) {
        return new RoleDto(roleID, roleName, Collections.emptySet());
    }

    public static Role getMockRole(RoleName roleName) {
        return getMockRole(null, roleName);
    }

    public static Role getMockRole(Long roleId, RoleName roleName) {
        return Role.builder()
                .roleId(roleId)
                .name(roleName.name())
                .users(Collections.emptySet())
                .build();
    }

    public static List<RoleDto> getMockRoleDtoList() {
        return List.of(
                getMockRoleDto(1L, RoleName.ROLE_USER),
                getMockRoleDto(2L, RoleName.ROLE_MODERATOR),
                getMockRoleDto(3L, RoleName.ROLE_ADMIN)
        );
    }

}
