package com.revilla.homestuff.service.imp;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.GeneralService;
import com.revilla.homestuff.util.GeneralUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * GeneralServiceImp
 *
 * @author Kirenai
 */
@Slf4j
@Service
public abstract class GeneralServiceImp<T, ID extends Serializable, E> implements GeneralService<T, ID> {

    private ModelMapper modelMapper;
    private Class<T> firstGeneric;
    private Class<E> thirdGeneric;

    @Autowired
    public final void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public final ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @SuppressWarnings("unchecked") // idk, but work
    public Class<T> getFirstGenericClass() {
        if (firstGeneric == null) {
            firstGeneric = (Class<T>) Objects.requireNonNull(GenericTypeResolver
                    .resolveTypeArguments(this.getClass(), GeneralServiceImp.class))[0];
        }
        return firstGeneric;
    }

    @SuppressWarnings("unchecked") // idk, but work
    public Class<E> getThirdGenericClass() {
        if (thirdGeneric == null) {
            thirdGeneric = (Class<E>) Objects.requireNonNull(GenericTypeResolver
                    .resolveTypeArguments(this.getClass(), GeneralServiceImp.class))[2];
        }
        return thirdGeneric;
    }

    @Override
    public List<T> findAll(Pageable pageable) {
        log.info("Calling the findAll method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        return this.getRepo().findAll(pageable)
                .getContent()
                .stream()
                .map(obj -> this.modelMapper.map(obj, this.getFirstGenericClass()))
                .collect(Collectors.toList());
    }

    @Override
    public T findOne(ID id, AuthUserDetails userDetails) {
        log.info("Calling the findOne method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        E obj = GeneralUtil.getEntityByIdOrThrow(id, this.getRepo(),
                this.getThirdGenericClass());
        GeneralUtil.validateAuthorizationPermissionOrThrow(obj, this.getRepo(), userDetails);
        return this.modelMapper.map(obj, this.getFirstGenericClass());
    }

    @Override
    @Transactional
    public T delete(ID id, AuthUserDetails userDetails) {
        log.info("Calling the delete method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        E obj = GeneralUtil.getEntityByIdOrThrow(id, this.getRepo(),
                this.getThirdGenericClass());
        GeneralUtil.validateAuthorizationPermissionOrThrow(obj, this.getRepo(), userDetails);
        this.getRepo().delete(obj);
        return this.modelMapper.map(obj, this.getFirstGenericClass());
    }

    public abstract JpaRepository<E, ID> getRepo();

}
