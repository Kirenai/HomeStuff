package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.request.LoginRequestDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthResource {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthUserDetails> login(@RequestBody @Valid LoginRequestDto request) {
        log.info("Invoking AuthResource.login method");
        return this.authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        log.info("Invoking AuthResource.register method");
        ApiResponseDto response = this.authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
