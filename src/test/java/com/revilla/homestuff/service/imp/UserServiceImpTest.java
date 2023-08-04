package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.mapper.user.UserMapper;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @InjectMocks
    private UserServiceImp userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    private User userMockOne;
    private User userMockTwo;
    private User userMockTree;
    private UserDto userDtoMockOne;
    private UserDto userDtoMockTwo;
    private UserDto userDtoMockToUpdate;

    @BeforeEach
    void setUp() {
        Long userIdOne = 1L;
        Long userIdTwo = 2L;
        Long userIdTree = 3L;

        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;

        this.userMockOne = UserServiceDataTestUtils.getUserMock(userIdOne, username,
                password, firstName, lastName, age);
        this.userMockTwo = UserServiceDataTestUtils.getUserMock(userIdTwo, username,
                password, firstName, lastName, age);
        this.userMockTree = UserServiceDataTestUtils.getUserMock(userIdTree, username,
                password, firstName, lastName, age);
        this.userDtoMockOne = UserServiceDataTestUtils.getUserDtoMock(userIdOne,
                username, password, firstName, lastName, age);
        this.userDtoMockTwo = UserServiceDataTestUtils.getUserDtoMock(userIdTwo, username,
                password, firstName, lastName, age);
        this.userDtoMockToUpdate = UserServiceDataTestUtils.getUserMockWithOutId(
                "KIRENAI",
                "KIRENAI",
                "KIRENAI",
                "KIRENAI",
                (byte) 23
        );
    }

    @Test
    @DisplayName("Should find a list user when we call find all")
    void shouldFindUserListWhenFindAll() {
        int sizeExpected = 2;
        Pageable pageableMock = mock(Pageable.class);
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(this.userMockOne, this.userMockTwo)));
        when(userMapper.mapOut(ArgumentMatchers.any()))
                .thenReturn(this.userDtoMockOne, this.userDtoMockTwo);

        List<UserDto> response = userService.findAll(pageableMock);

        assertNotNull(response);
        assertEquals(sizeExpected, response.size());

        verify(userRepository, times(1)).findAll(pageableMock);
        verify(userMapper, times(2)).mapOut(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should throw exception when user don't exists by id when find one")
    void shouldThrowExceptionWhenUserNotExistsByIdWhenFindOne() {
        Long userIdToFind = 1L;
        String expectedMessage = "User not found with id: " + userIdToFind;
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> userService.findOne(userIdToFind, null));

        assertEquals(ex.getMessage(), expectedMessage);

        verify(userRepository, times(1)).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should find a user when it exists by id when find one")
    void shouldFindUserWhenExistsByIdWhenFindOne() {
        Long userIdToFind = 1L;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userMockOne));
        when(userMapper.mapOut(any())).thenReturn(userDtoMockOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockOne);

        UserDto userDto = userService.findOne(userIdToFind, userDetails);

        assertEquals(userDto, userDtoMockOne);

        verify(userRepository, times(1)).findById(anyLong());
        verify(this.userMapper, times(1)).mapOut(any());
    }


    @Test
    @DisplayName("Should throw an exception when username is already exits")
    void shouldThrowExceptionWhenUsernameAlreadyExits() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " is already exists with name: " + this.userDtoMockOne.getUsername();

        when(this.userRepository.existsByName(Mockito.anyString())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.userService.create(this.userDtoMockOne));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository, times(1)).existsByName(anyString());
    }

    @Test
    @DisplayName("Should create an user")
    void shouldCreateUser() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " created successfully by admin";
        when(this.userMapper.mapIn(any())).thenReturn(this.userMockOne);
        when(this.userRepository.save(any(User.class))).thenReturn(this.userMockOne);
        when(this.userMapper.mapOut(any())).thenReturn(this.userDtoMockOne);

        UserDto userSaved = this.userService.create(this.userDtoMockOne);

        assertEquals(expected, userSaved.getMessage());

        verify(this.userMapper, times(1)).mapIn(any());
        verify(this.userRepository, times(1)).save(any());
        verify(this.userMapper, times(1)).mapOut(any());
    }

    @Test
    @DisplayName("Should throw an exception when user is not found by id when updating")
    void shouldThrowExceptionWhenUserIsNotFoundById() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " not found with id: " + this.userDtoMockOne.getUserId();

        when(this.userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                assertThrows(EntityNoSuchElementException.class,
                        () -> this.userService.update(userIdToFind, null, null));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw an exception when unauthorized by user and userDetails id when updating")
    void shouldThrowExceptionWhenUnauthorized() {
        Long userIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.UPDATE.name() + " this profile";

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockTree);

        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userMockOne));

        UnauthorizedPermissionException ex =
                assertThrows(UnauthorizedPermissionException.class,
                        () -> this.userService.update(userIdToFind, null, userDetails));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should update an user when have authorization")
    void shouldUpdateUserWhenHaveAuthorization() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " updated successfully";

        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userMockOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockOne);

        ApiResponseDto response = this.userService.update(userIdToFind,
                this.userDtoMockToUpdate, userDetails);

        assertEquals(expected, response.getMessage());
        assertTrue(response.getSuccess());

        verify(this.userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should update an user with roles when have authorization")
    void shouldUpdateUserWhenHaveAuthorizationWithRoles() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " updated successfully";

        Role roleMock = RoleServiceDataTestUtils.getRoleMock(1L, RoleName.ROLE_ADMIN);
        this.userMockOne.setRoles(Set.of(roleMock));

        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userMockOne));
        when(this.roleRepository.findByName(any(String.class)))
                .thenReturn(Optional.of(roleMock));

        Role roleMockAdmin = RoleServiceDataTestUtils.getRoleMock(1L, RoleName.ROLE_ADMIN);
        this.userMockTwo.setRoles(Set.of(roleMockAdmin));
        AuthUserDetails userDetails = new AuthUserDetails(this.userMockTwo);

        RoleDto roleDtoMock = RoleServiceDataTestUtils.getRoleDtoMock(1L, RoleName.ROLE_ADMIN);
        this.userDtoMockToUpdate.setRoles(Set.of(roleDtoMock));

        ApiResponseDto response = this.userService.update(userIdToFind,
                this.userDtoMockToUpdate, userDetails);

        assertEquals(expected, response.getMessage());
        assertTrue(response.getSuccess());

        verify(this.userRepository, times(1)).findById(anyLong());
        verify(this.roleRepository, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("Should throw an exception when user not found when deleting")
    void shouldThrowExceptionWhenUserNotFoundWhenDeleting() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " not found with id: " + userIdToFind;

        when(this.userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                assertThrows(EntityNoSuchElementException.class,
                        () -> this.userService.delete(userIdToFind, null));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized when deleting")
    void shouldThrowExceptionWhenUserIsUnauthorizedWhenDeleting() {
        Long userIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.DELETE.name() + " this profile";

        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userMockOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockTwo);

        UnauthorizedPermissionException ex =
                assertThrows(UnauthorizedPermissionException.class,
                        () -> this.userService.delete(userIdToFind, userDetails));

        assertEquals(expected, ex.getMessage());

        verify(this.userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should delete an user when deleting")
    void shouldDeleteUserWhenDeleting() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " deleted successfully";

        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.userMockOne));
        Mockito.doNothing().when(this.userRepository).delete(this.userMockOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockOne);

        ApiResponseDto response = this.userService.delete(userIdToFind, userDetails);

        assertEquals(expected, response.getMessage());
        assertTrue(response.getSuccess());

        verify(this.userRepository, times(1)).findById(anyLong());
        verify(this.userRepository, times(1)).delete(any());
    }

}