package com.revilla.homestuff.util;

import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
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
        }
        if (repo instanceof RoleRepository) {
            isDuplicated = ((RoleRepository) repo).existsByName(RoleName.valueOf(toValidate));
        }
        if (repo instanceof NourishmentRepository) {
            isDuplicated = ((NourishmentRepository) repo).existsByName(toValidate);
        }

        if (isDuplicated) {
            throw new EntityDuplicateConstraintViolationException(
                    GeneralUtil.simpleNameClass(entityClass)
                            + " is already exists with name: " + toValidate
            );
        }
    }

    public static <E, ID> void validateAuthorizationPermissionOrThrow(
            @NotNull E obj,
            @NotNull JpaRepository<E, ID> repo,
            @NotNull AuthUserDetails userDetails) {
        String errorMessage = null;
        if (obj instanceof User) {
            if (((User) obj).getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                            .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to access this profile";
        }
        if (obj instanceof Nourishment) {
            if (((Nourishment) obj).getUser().getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                            .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to access this nourishment";
        }
        if (obj instanceof Role) {
            if (userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to access this role";
        }
        throw new UnauthorizedPermissionException(errorMessage);
    }

    public static <T> T addResponseMessageDeleteAction(@NotNull T obj,
            @NotNull Class<T> clazz) {
        if (obj instanceof UserDto) {
            UserDto userDto = ((UserDto) obj)
                    .setMessage(simpleNameClass(User.class) + " successfully removed");
            return clazz.cast(userDto);
        }
        if (obj instanceof NourishmentDto) {
            NourishmentDto nourishmentDto = ((NourishmentDto) obj)
                    .setMessage(simpleNameClass(Nourishment.class) + " successfully removed");
            return clazz.cast(nourishmentDto);
        }
        return null;
    }

    public static String simpleNameClass(@NotNull Class<?> clazzGeneric) {
        return clazzGeneric.getSimpleName();
    }

}
