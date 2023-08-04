package com.revilla.homestuff.mapper;

public interface GenericMapper<T, E> {

    E mapIn(T dto);

    T mapOut(E entity);

}
