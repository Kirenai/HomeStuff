package com.revilla.homestuff.util;

import com.revilla.homestuff.dto.only.RoleDtoOnly;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.RoleRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NourishmentUtil
 *
 * @author Kirenai
 */
public class RoleUtil {

    public static Role getRoleByNameOrThrow(String name,
                                            RoleRepository repo) {
        return repo.findByName(name)
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(Role.class)
                                + " don't found with username: " + name
                ));
    }

    public static Set<Role> getSetOfRolesOrThrow(Collection<RoleDtoOnly> roleDto,
                                                 RoleRepository repo) {
        if (Objects.isNull(roleDto)) {
            return Set.of(getRoleByNameOrThrow("ROLE_USER", repo));
        }
        return roleDto.stream()
                .map(RoleDtoOnly::getName)
                .map(name -> getRoleByNameOrThrow(name, repo))
                .collect(Collectors.toSet());
    }

}
