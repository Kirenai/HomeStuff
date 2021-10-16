package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    private User userMock;
    private User userMockAuthSameData;
    private User userMockAuthDifferentData;
    private UserDto userDtoMock;
    private UserDto userDtoMockToUpdate;

    @BeforeEach
    void setUp() {
        Long userId = 1L;
        Long userIdAuthSame = 1L;
        Long userIdAuthDifferent = 2L;
        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;
        this.userMock = UserServiceDataTestUtils.getMockUser(userId, username,
                password, firstName, lastName, age);
        this.userDtoMock = UserServiceDataTestUtils.getMockUserDto(userId,
                username, password, firstName, lastName, age);
        this.userMockAuthDifferentData = UserServiceDataTestUtils
                .getMockUser(userIdAuthDifferent, username, password,
                        firstName, lastName, age);
        this.userMockAuthSameData = UserServiceDataTestUtils
                .getMockUser(userIdAuthSame, username, password, firstName,
                        lastName, age);
        this.userDtoMockToUpdate = UserServiceDataTestUtils.getMockUserWithOutId(
                "KIRENAI",
                "KIRENAI",
                "KIRENAI",
                "KIRENAI",
                (byte) 23
        );
    }

    @Test
    @DisplayName("Should throw an exception when username is already exits")
    void shouldThrowExceptionWhenUsernameAlreadyExits() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " is already exists with name: " + this.userDtoMock.getUsername();

        when(this.userRepository.existsByUsername(anyString())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.userService.create(this.userDtoMock));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository).existsByUsername(this.userDtoMock.getUsername());
    }

    @Test
    @DisplayName("Should create a user")
    void shouldCreateUser() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " created successfully by admin";
        when(this.modelMapper.map(this.userDtoMock, User.class)).thenReturn(this.userMock);
        when(this.userRepository.save(any(User.class))).thenReturn(this.userMock);
        when(this.modelMapper.map(this.userMock, UserDto.class)).thenReturn(this.userDtoMock);

        UserDto userSaved = this.userService.create(this.userDtoMock);

        assertEquals(expected, userSaved.getMessage());

        verify(this.modelMapper).map(this.userDtoMock, User.class);
        verify(this.userRepository).save(this.userMock);
        verify(this.modelMapper).map(this.userMock, UserDto.class);
    }

    @Test
    @DisplayName("Should throw a exception when user is not found by id when updating")
    void shouldThrowExceptionWhenUserIsNotFoundById() {
        Long userIdToFind  = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " don't found with id: " + this.userDtoMock.getUserId();

        when(this.userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                assertThrows(EntityNoSuchElementException.class,
                        () -> this.userService.update(userIdToFind, null, null));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw a exception when Unauthorized by user and userDetails id")
    void shouldThrowExceptionWhenUnauthorized() {
        Long userIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.UPDATE.name() + " this profile";

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockAuthDifferentData);

        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userMock));

        UnauthorizedPermissionException ex =
                assertThrows(UnauthorizedPermissionException.class,
                        () -> this.userService.update(userIdToFind, null, userDetails));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should update an user when have authorization")
    void shouldUpdateUserWhenHaveAuthorization() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " updated successfully";

        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userMock));

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockAuthSameData);

        ApiResponseDto response = this.userService.update(userIdToFind,
                this.userDtoMockToUpdate, userDetails);

        assertEquals(expected, response.getMessage());
        assertTrue(response.getSuccess());

        verify(this.userRepository).findById(userIdToFind); 
    }
}