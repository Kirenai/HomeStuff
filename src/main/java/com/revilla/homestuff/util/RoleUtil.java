package com.revilla.homestuff.util;

import com.revilla.homestuff.dto.only.RoleDtoOnly;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.util.enums.RoleName;

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

    public static Role getRoleByNameOrThrow(RoleName name,
                                            RoleRepository repo) {
        return repo.findByName(name.name())
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(Role.class)
                                + " don't found with username: " + name
                ));
    }

    /**
     *
     * @param roleDto A Collection of {@link RoleDtoOnly} or if is null return ROLE_USER
     * @param repo Role repository
     * @return a Set of just one role or Set of roles
     */
    public static Set<Role> getSetOfRolesOrThrow(Collection<RoleDtoOnly> roleDto,
                                                 RoleRepository repo) {
        if (Objects.isNull(roleDto)) {
            return Set.of(getRoleByNameOrThrow(RoleName.ROLE_USER, repo));
        }
        return roleDto.stream()
                .map(RoleDtoOnly::getName)
                .map(name -> getRoleByNameOrThrow(name, repo))
                .collect(Collectors.toSet());
    }

}
