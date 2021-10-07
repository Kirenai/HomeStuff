package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.GeneralUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static com.revilla.homestuff.entity.User.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImpTest {

    @Autowired
    @Qualifier("user.service")
    private UserService userServiceImp;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ModelMapper modelMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        Long userId = 1L;
        String username = "Kirenai";
        String password = "kirenai";
        String firstName = "Kirenai";
        String lastName = "Kirenai";
        Byte age = 22;

        user = builder()
                .userId(userId)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .roles(Collections.emptySet())
                .build();

        userDto = new UserDto(
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

    @Test
    void createTest() {
        when(this.modelMapper.map(userDto, User.class)).thenReturn(user);
        when(this.userRepository.save(user)).thenReturn(user);
        when(this.modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto userSaved = this.userServiceImp.create(userDto);
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " created successfully by admin";
        assertEquals(expected, userSaved.getMessage());
    }

}