package com.revilla.homestuff.utils.dto.request;

import com.revilla.homestuff.dto.request.RegisterRequestDto;

public class RegisterRequestDtoDataTest {

    public static RegisterRequestDto getMockRegisterRequestDto(
            String username,
            String password,
            String firstName,
            String lastName,
            Byte age) {
        return new RegisterRequestDto(username, password, firstName, lastName, age);
    }

}
