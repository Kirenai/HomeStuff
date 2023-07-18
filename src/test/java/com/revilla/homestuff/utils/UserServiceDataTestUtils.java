package com.revilla.homestuff.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;

import java.io.IOException;
import java.util.Collections;


public class UserServiceDataTestUtils {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final UserServiceDataTestUtils userServiceDataTestUtils = new UserServiceDataTestUtils();

    public static UserServiceDataTestUtils getInstance() {
        return userServiceDataTestUtils;
    }

    public static UserDto getUserDtoMock(Long userId,
                                         String username,
                                         String password,
                                         String firstName,
                                         String lastName,
                                         Byte age) {
        return UserDto.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .nourishments(Collections.emptyList())
                .consumptions(Collections.emptyList())
                .roles(Collections.emptySet())
                .build();
    }

    public UserDto getUserDto() throws IOException {
        return mapper.readValue(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("mock/user/UserDto.json"), UserDto.class);
    }

    public User getUser() throws IOException {
        return mapper.readValue(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("mock/user/User.json"), User.class);
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
        return UserDto.builder()
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .build();
    }

}
