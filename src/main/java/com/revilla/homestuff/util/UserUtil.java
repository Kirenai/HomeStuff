package com.revilla.homestuff.util;

import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import org.jetbrains.annotations.NotNull;

public class UserUtil {

    public static User getUserOrThrow(Long userId, @NotNull UserRepository repo) {
        return repo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User don't found"));
    }

}
