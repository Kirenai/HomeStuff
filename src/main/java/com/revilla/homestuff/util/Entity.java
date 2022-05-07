package com.revilla.homestuff.util;

import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public class Entity {

    public static <E, ID extends Serializable, R extends JpaRepository<E, ID>> E getById(
            @NotNull ID id,
            @NotNull R repo,
            @NotNull Class<E> entityClass) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(entityClass)
                                + " not found with id: " + id)
                );
    }

}
