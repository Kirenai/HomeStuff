package com.revilla.homestuff.util;

import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.NourishmentRepository;
import org.jetbrains.annotations.NotNull;

/**
 * NourishmentUtil
 * @author Kirenai
 */
public class NourishmentUtil {

    public static void validateConstraintViolation(
            @NotNull String name,
            @NotNull NourishmentRepository repo) {
        if (repo.existsByName(name)) {
            throw new EntityDuplicateConstraintViolationException(
                    GeneralUtil.simpleNameClass(Nourishment.class)
                            + " is already exists with name: " + name
            );
        }
    }

}
