package com.revilla.homestuff.util;

import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * GeneralUtil
 *
 * @author Kirenai
 */
public class GeneralUtil {

    public static <E, ID extends Serializable, R extends JpaRepository<E, ID>> E getEntityByIdOrThrow(
            @NotNull ID id,
            @NotNull R repo,
            @NotNull Class<E> entityClass) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(entityClass)
                                + " don't found with id: " + id)
                );
    }

    public static <E> void validateDuplicateConstraintViolation(
            @NotNull String toValidate,
            @NotNull JpaRepository<E, Long> repo,
            @NotNull Class<E> entityClass) {
        Boolean isDuplicated = false;

        if (repo instanceof UserRepository) {
            isDuplicated = ((UserRepository) repo).existsByUsername(toValidate);
        } else if (repo instanceof RoleRepository) {
            isDuplicated = ((RoleRepository) repo).existsByName(toValidate);
        } else if (repo instanceof NourishmentRepository) {
            isDuplicated = ((NourishmentRepository) repo).existsByName(toValidate);
        }

        if (isDuplicated) {
            throw new EntityDuplicateConstraintViolationException(
                    GeneralUtil.simpleNameClass(entityClass)
                            + " is already exists with name: " + toValidate
            );
        }
    }

    public static String simpleNameClass(@NotNull Class<?> classGeneric) {
        return classGeneric.getSimpleName();
    }

}
