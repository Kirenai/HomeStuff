package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.request.LoginRequestDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.AuthService;
import com.revilla.homestuff.util.ConstraintViolation;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.RoleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<AuthUserDetails> login(LoginRequestDto loginRequestDto) {
        log.info("Invoking AuthServiceImp.login method");
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        String token = this.jwtTokenProvider.generateJwtToken(authentication);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, this.jwtTokenProvider.getTokenPrefix() + token)
                .body(userDetails);
    }

    @Transactional
    @Override
    public ApiResponseDto register(RegisterRequestDto requestDto) {
        log.info("Invoking AuthServiceImp.register method");
        ConstraintViolation.validateDuplicate(requestDto.getUsername(),
                this.userRepository, User.class);
        User user = this.modelMapper.map(requestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(RoleUtil.getSetOfRolesOrThrow(null, this.roleRepository));
        this.userRepository.save(user);
        return GeneralUtil.responseMessageAction(User.class, "registered successfully");
    }

}
