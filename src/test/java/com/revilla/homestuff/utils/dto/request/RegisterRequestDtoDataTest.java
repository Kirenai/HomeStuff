package com.revilla.homestuff.utils.dto.request;

import com.revilla.homestuff.dto.request.RegisterRequestDto;

public class RegisterRequestDtoDataTest {

    public static RegisterRequestDto getMockRegisterRequestDto(
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

    public static RegisterRequestDto getMockRegisterRequestDto() {
        return getMockRegisterRequestDto(
                "kirenai",
                "kirenai361",
                "kirenai",
                "kirenai",
                (byte) 22);
    }

}
