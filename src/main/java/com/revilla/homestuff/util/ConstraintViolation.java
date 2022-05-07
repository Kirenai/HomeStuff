package com.revilla.homestuff.util;

import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.ExitsByProperty;
import org.jetbrains.annotations.NotNull;

public class ConstraintViolation {

    public static <E> void validateDuplicate(
            @NotNull String toValidate,
            @NotNull ExitsByProperty repo,
            @NotNull Class<E> entityClass) {

        Boolean isDuplicated = repo.existsByName(toValidate);

        if (isDuplicated) {
            throw new EntityDuplicateConstraintViolationException(
                    GeneralUtil.simpleNameClass(entityClass)
                            + " is already exists with name: " + toValidate
            );
        }
    }

}
