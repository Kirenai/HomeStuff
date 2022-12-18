package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import com.revilla.homestuff.utils.dto.request.RegisterRequestDtoDataTest;
import com.revilla.homestuff.utils.dto.response.ApiResponseDataTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImpTest {

    @InjectMocks
    private AuthServiceImp authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;

    private User userMockOne;
    private RegisterRequestDto registerDtoMock;

    @BeforeEach
    void setUp() {
        Long userIdOne = 1L;

        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;

        this.userMockOne = UserServiceDataTestUtils.getUserMock(userIdOne, username,
                password, firstName, lastName, age);

        this.registerDtoMock = RegisterRequestDtoDataTest
                .getRegisterRequestDtoMock(username, password, firstName, lastName, age);
    }

    @Test
    @DisplayName("Should throw an exception when a user wants to register with and existing username")
    void shouldThrowExceptionWhenUserWantRegisterWithExistingUsername() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " is already exists with name: " + this.registerDtoMock.getUsername();

        when(this.userRepository.existsByName(anyString())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                Assertions.assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.authService.register(this.registerDtoMock));

        Assertions.assertEquals(expected, ex.getMessage());

        verify(this.userRepository, times(1)).existsByName(this.registerDtoMock.getUsername());
    }

    @Test
    @DisplayName("Should register an user")
    void shouldRegisterUser() {
        ApiResponseDto expected = ApiResponseDataTestUtils
                .getApiResponseMock("registered successfully", User.class);
        Role roleMock = RoleServiceDataTestUtils.getRoleMock(1L, RoleName.ROLE_USER);

        when(this.userRepository.existsByName(anyString())).thenReturn(false);
        when(this.modelMapper.map(this.registerDtoMock, User.class)).thenReturn(this.userMockOne);
        when(this.roleRepository.findByName(any(String.class))).thenReturn(Optional.of(roleMock));
        when(this.userRepository.save(any(User.class))).thenReturn(this.userMockOne);

        ApiResponseDto response = this.authService.register(this.registerDtoMock);

        Assertions.assertEquals(expected.getMessage(), response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        verify(this.userRepository).existsByName(this.registerDtoMock.getUsername());
        verify(this.modelMapper).map(this.registerDtoMock, User.class);
        verify(this.roleRepository).findByName(roleMock.getName());
        verify(this.userRepository).save(this.userMockOne);
    }

}
