package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;

import java.util.Collections;


public class UserServiceDataTestUtils {

    public static UserDto getUserDtoMock(Long userId,
                                         String username,
                                         String password,
                                         String firstName,
                                         String lastName,
                                         Byte age) {
        return new UserDto(
                userId,
                username,
                password,
                firstName,
                lastName,
                age,
                Collections.emptySet(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    public static User getUserMock(String username,
                                   String password,
                                   String firstName,
                                   String lastName,
                                   Byte age) {
        return getUserMock(null, username, password, firstName, lastName, age);
    }

    public static User getUserMock(Long userId,
                                   String username,
                                   String password,
                                   String firstName,
                                   String lastName,
                                   Byte age) {
        return User.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .roles(Collections.emptySet())
                .build();
    }

    public static UserDto getUserMockWithOutId(String username,
                                               String password,
                                               String firstName,
                                               String lastName,
                                               Byte age) {
        return new UserDto(
                username,
                password,
                firstName,
                lastName,
                age
        );
    }

}
