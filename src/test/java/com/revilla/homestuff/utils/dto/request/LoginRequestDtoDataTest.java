package com.revilla.homestuff.utils.dto.request;

import com.revilla.homestuff.dto.request.LoginRequestDto;

public class LoginRequestDtoDataTest {

    public static final String USERNAME = "kirenai";
    public static final String PASSWORD = "kirenai361";

    public static LoginRequestDto getLoginRequestDtoMock() {
        return new LoginRequestDto(USERNAME, PASSWORD);
    }

}
