package com.revilla.homestuff.service.imp;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.GeneralService;
import com.revilla.homestuff.util.Entity;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import org.modelmapper.ModelMapper;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static com.revilla.homestuff.util.GeneralUtil.simpleNameClass;

/**
 * GeneralServiceImp
 *
 * @author Kirenai
 */
@Slf4j
@Service
public abstract class GeneralServiceImp<T, ID extends Serializable, E> implements GeneralService<T, ID> {

    private Class<T> firstGeneric;
    private Class<E> thirdGeneric;

    public Class<T> getFirstGenericClass() {
        if (firstGeneric == null) {
            firstGeneric = (Class<T>) Objects.requireNonNull(GenericTypeResolver
                    .resolveTypeArguments(this.getClass(), GeneralServiceImp.class))[0];
        }
        return firstGeneric;
    }

    public Class<E> getThirdGenericClass() {
        if (thirdGeneric == null) {
            thirdGeneric = (Class<E>) Objects.requireNonNull(GenericTypeResolver
                    .resolveTypeArguments(this.getClass(), GeneralServiceImp.class))[2];
        }
        return thirdGeneric;
    }

    @Override
    public List<T> findAll(Pageable pageable) {
        log.info("Invoking {}.findAll method", simpleNameClass(this.getClass()));
        return this.getRepo().findAll(pageable)
                .getContent()
                .stream()
                .map(obj -> this.getModelMapper().map(obj, this.getFirstGenericClass()))
                .collect(Collectors.toList());
    }

    @Override
    public T findOne(ID id, AuthUserDetails userDetails) {
        log.info("Invoking {}.findOne method", simpleNameClass(this.getClass()));
        E obj = Entity.getById(id, this.getRepo(), this.getThirdGenericClass());
        GeneralUtil.validateAuthorizationPermissionOrThrow(obj, userDetails, MessageAction.ACCESS);
        return this.getModelMapper().map(obj, this.getFirstGenericClass());
    }

    @Override
    @Transactional
    public ApiResponseDto delete(ID id, AuthUserDetails userDetails) {
        log.info("Invoking {}.delete method", simpleNameClass(this.getClass()));
        E obj = Entity.getById(id, this.getRepo(), this.getThirdGenericClass());
        GeneralUtil.validateAuthorizationPermissionOrThrow(obj, userDetails, MessageAction.DELETE);
        this.getRepo().delete(obj);
        return GeneralUtil.responseMessageAction(this.getThirdGenericClass(),
                "deleted successfully");
    }

    public abstract JpaRepository<E, ID> getRepo();

    public abstract ModelMapper getModelMapper();

}
