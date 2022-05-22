package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.request.LoginRequestDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.AuthService;
import com.revilla.homestuff.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthResource {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthUserDetails> login(@RequestBody @Valid LoginRequestDto request) {
        return this.authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        ApiResponseDto response = this.authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
