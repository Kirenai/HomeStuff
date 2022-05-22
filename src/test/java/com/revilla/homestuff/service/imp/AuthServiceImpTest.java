package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.AuthService;
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceImpTest {

    @Autowired
    private AuthService authService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
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

        this.userMockOne = UserServiceDataTestUtils.getMockUser(userIdOne, username,
                password, firstName, lastName, age);

        this.registerDtoMock = RegisterRequestDtoDataTest
                .getMockRegisterRequestDto(username, password, firstName, lastName, age);
    }

    @Test
    @DisplayName("Should throw an exception when a user wants to register with and existing username")
    void shouldThrowExceptionWhenUserWantRegisterWithExistingUsername() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " is already exists with name: " + this.registerDtoMock.getUsername();

        Mockito.when(this.userRepository.existsByName(Mockito.anyString())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                Assertions.assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.authService.register(this.registerDtoMock));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.userRepository).existsByName(this.registerDtoMock.getUsername());
    }

    @Test
    @DisplayName("Should register an user")
    void shouldRegisterUser() {
        ApiResponseDto expected = ApiResponseDataTestUtils
                .getApiResponseMock("registered successfully", User.class);
        Role roleMock = RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_USER);

        Mockito.when(this.userRepository.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(this.modelMapper.map(this.registerDtoMock, User.class)).thenReturn(this.userMockOne);
        Mockito.when(this.roleRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(roleMock));
        Mockito.when(this.userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(this.userMockOne);

        ApiResponseDto response = this.authService.register(this.registerDtoMock);

        Assertions.assertEquals(expected.getMessage(), response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        Mockito.verify(this.userRepository).existsByName(this.registerDtoMock.getUsername());
        Mockito.verify(this.modelMapper).map(this.registerDtoMock, User.class);
        Mockito.verify(this.roleRepository).findByName(roleMock.getName());
        Mockito.verify(this.userRepository).save(this.userMockOne);
    }

}
