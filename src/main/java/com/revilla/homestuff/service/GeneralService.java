package com.revilla.homestuff.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GeneralService<T, ID> {

    List<T> findAll(Pageable pageable);

    T findOne(ID id);

    T create(T data);

    T update(ID id, T data);

    T delete(ID id);

}
