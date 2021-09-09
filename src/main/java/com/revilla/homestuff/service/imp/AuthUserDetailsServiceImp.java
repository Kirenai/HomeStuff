package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Qualifier("user.details.service")
@RequiredArgsConstructor
public class AuthUserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserUtil.getUserDetailsOrThrow(username, this.userRepository);
    }

}
