package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.request.LoginRequestDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<AuthUserDetails> login(LoginRequestDto loginRequestDto);

    ApiResponseDto register(RegisterRequestDto requestDto);

}
