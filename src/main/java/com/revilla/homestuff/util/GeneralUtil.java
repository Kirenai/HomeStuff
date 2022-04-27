package com.revilla.homestuff.util;

import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.*;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.ExitsByProperty;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.util.enums.RoleName;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
                                + " not found with id: " + id)
                );
    }

    public static <E> void validateDuplicateConstraintViolation(
            @NotNull String toValidate,
            @NotNull ExitsByProperty repo,
            @NotNull Class<E> entityClass) {

        Boolean isDuplicated = repo.existsByName(toValidate);

        if (isDuplicated) {
            throw new EntityDuplicateConstraintViolationException(
                    GeneralUtil.simpleNameClass(entityClass)
                            + " is already exists with name: " + toValidate
            );
        }
    }

    public static <E> void validateAuthorizationPermissionOrThrow(
            @NotNull E obj,
            @NotNull AuthUserDetails userDetails,
            @NotNull MessageAction action) {
        String errorMessage = null;
        if (obj instanceof User user) {
            if (user.getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this profile";
        }
        if (obj instanceof Nourishment nourishment) {
            if (nourishment.getUser().getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this nourishment";
        }
        if (obj instanceof Consumption consumption) {
            if (consumption.getUser().getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this consumption";
        }
        if (obj instanceof Role) {
            if (userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this role";
        }
        throw new UnauthorizedPermissionException(errorMessage);
    }

    public static <E> ApiResponseDto responseMessageAction(@NotNull E obj,
                                                           @NotNull Class<E> clazz,
                                                           @NotNull String messageAction) {
        StringBuilder message = new StringBuilder();
        if (obj instanceof User) {
            message.append(simpleNameClass(clazz)).append(" ").append(messageAction);
        }
        if (obj instanceof Nourishment) {
            message.append(simpleNameClass(clazz)).append(" ").append(messageAction);
        }
        if (obj instanceof Role) {
            message.append(simpleNameClass(clazz)).append(" ").append(messageAction);
        }
        if (obj instanceof Category) {
            message.append(simpleNameClass(clazz)).append(" ").append(messageAction);
        }
        return new ApiResponseDto(Boolean.TRUE, message.toString());
    }

    public static String simpleNameClass(@NotNull Class<?> clazzGeneric) {
        return clazzGeneric.getSimpleName();
    }

}
