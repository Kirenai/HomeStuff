package com.revilla.homestuff.util;

import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

@Slf4j
public class Entity {

    public static <E, ID extends Serializable, R extends JpaRepository<E, ID>> E getById(
            @NotNull ID id,
            @NotNull R repo,
            @NotNull Class<E> entityClass) {
        log.info("Invoking Entity.getById method");
        return repo.findById(id)
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(entityClass)
                                + " not found with id: " + id)
                );
    }

}
