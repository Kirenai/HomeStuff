package com.revilla.homestuff.utils.dto.request;

import com.revilla.homestuff.dto.request.RegisterRequestDto;

public class RegisterRequestDtoDataTest {

    public static RegisterRequestDto getRegisterRequestDtoMock(
            String username,
            String password,
            String firstName,
            String lastName,
            Byte age) {
        return RegisterRequestDto.builder()
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .build();
    }

    public static RegisterRequestDto getRegisterRequestDtoMock() {
        return getRegisterRequestDtoMock(
                "kirenai",
                "kirenai361",
                "kirenai",
                "kirenai",
                (byte) 22);
    }

}
