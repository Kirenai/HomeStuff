package com.revilla.homestuff.service;

import org.springframework.data.domain.Pageable;
import java.io.Serializable;
import java.util.List;

public interface GeneralService<T, ID extends Serializable> {

    List<T> findAll(Pageable pageable);

    T findOne(ID id);

    T delete(ID id);

}
