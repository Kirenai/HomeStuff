package com.revilla.homestuff.util;

import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * GeneralUtil
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

    public static String simpleNameClass(@NotNull Class<?> classGeneric) {
        return classGeneric.getSimpleName();
    }

}
