package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImpTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ModelMapper modelMapper;

    private final Long userId = 1L;
    private final String username = "kirenai";
    private final String password = "kirenai";
    private final String firstName = "kirenai";
    private final String lastName = "kirenai";
    private final Byte age = 22;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Should throw an exception when username is already exits")
    void shouldThrowExceptionWhenUsernameAlreadyExits() {
        var userDtoMock = UserServiceDataTestUtils.getMockUserDto(userId, username,
                password, firstName, lastName, age);
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " is already exists with name: " + userDtoMock.getUsername();

        when(this.userRepository.existsByUsername(anyString())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.userService.create(userDtoMock));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository).existsByUsername(userDtoMock.getUsername());
    }

    @Test
    void createTest() {
        var user = UserServiceDataTestUtils.getMockUser(userId, username, password, firstName, lastName, age);
        var userDto = UserServiceDataTestUtils.getMockUserDto(userId, username, password, firstName, lastName, age);

        when(this.modelMapper.map(userDto, User.class)).thenReturn(user);
        when(this.userRepository.save(user)).thenReturn(user);
        when(this.modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto userSaved = this.userService.create(userDto);
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " created successfully by admin";
        assertEquals(expected, userSaved.getMessage());

        Mockito.verify(this.modelMapper).map(userDto, User.class);
        Mockito.verify(this.userRepository).save(user);
        Mockito.verify(this.modelMapper).map(user, UserDto.class);
    }

}