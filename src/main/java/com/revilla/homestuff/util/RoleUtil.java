package com.revilla.homestuff.util;

import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.RoleRepository;
import org.jetbrains.annotations.NotNull;

/**
 * NourishmentUtil
 * @author Kirenai
 */
public class RoleUtil {

    public static void validateConstraintViolation(
            @NotNull String name,
            @NotNull RoleRepository repo) {
        if (repo.existsByName(name)) {
            throw new EntityDuplicateConstraintViolationException(
                    GeneralUtil.simpleNameClass(Role.class)
                            + " is already exists with name: " + name
            );
        }
    }

}
