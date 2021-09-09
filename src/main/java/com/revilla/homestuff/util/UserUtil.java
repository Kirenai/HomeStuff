package com.revilla.homestuff.util;

import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserUtil
 *
 * @author Kirenai
 */
public class UserUtil {

    public static UserDetails getUserDetailsOrThrow(String username, UserRepository repo) {
        return repo.findByUsername(username)
                .map(AuthUserDetails::new)
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(User.class)
                                + " don't found with username: " + username
                ));
    }

}
