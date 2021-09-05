package com.revilla.homestuff.util;

import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.UserRepository;
import org.jetbrains.annotations.NotNull;

/**
 * UserUtil
 * @author Kirenai
 */
public class UserUtil {

    public static void validateConstraintViolation(
            @NotNull String username,
            @NotNull UserRepository repo) {
        if (repo.existsByUsername(username)) {
            throw new EntityDuplicateConstraintViolationException(
                    GeneralUtil.simpleNameClass(User.class)
                            + " is already exists with name: " + username
            );
        }
    }

}
