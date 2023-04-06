package com.revilla.homestuff.util;

import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserUtil
 *
 * @author Kirenai
 */
@Slf4j
public class UserUtil {

    public static UserDetails getUserDetailsOrThrow(String username, UserRepository repo) {
        log.info("Invoking RoleUtil.getSetOfRolesOrThrow method");
        return repo.findByUsername(username)
                .map(AuthUserDetails::new)
                .orElseThrow(() -> new BadCredentialsException(
                        GeneralUtil.simpleNameClass(User.class)
                                + " not found with username: " + username
                ));
    }

}
