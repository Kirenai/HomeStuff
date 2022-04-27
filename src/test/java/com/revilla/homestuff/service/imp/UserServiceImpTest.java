package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private User userMockOne;
    private User userMockTwo;
    private User userMockTree;
    private UserDto userDtoMockOne;
    private UserDto userDtoMockTwo;
    private UserDto userDtoMockToUpdate;
    private RegisterRequestDto registerDtoMock;

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

        this.userMockOne = UserServiceDataTestUtils.getMockUser(userIdOne, username,
                password, firstName, lastName, age);
        this.userMockTwo = UserServiceDataTestUtils.getMockUser(userIdTwo, username,
                password, firstName, lastName, age);
        this.userMockTree = UserServiceDataTestUtils.getMockUser(userIdTree, username,
                password, firstName, lastName, age);
        this.userDtoMockOne = UserServiceDataTestUtils.getMockUserDto(userIdOne,
                username, password, firstName, lastName, age);
        this.userDtoMockTwo = UserServiceDataTestUtils.getMockUserDto(userIdTwo, username,
                password, firstName, lastName, age);
        this.userDtoMockToUpdate = UserServiceDataTestUtils.getMockUserWithOutId(
                "KIRENAI",
                "KIRENAI",
                "KIRENAI",
                "KIRENAI",
                (byte) 23
        );
        this.registerDtoMock = RegisterRequestDtoDataTest
                .getMockRegisterRequestDto(username, password, firstName, lastName, age);
    }

    @Test
    @DisplayName("Should find a list user when we call find all")
    void shouldFindUserListWhenFindAll() {
        int sizeExpected = 2;
        Pageable pageableMock = Mockito.mock(Pageable.class);
        Mockito.when(userRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(this.userMockOne, this.userMockTwo)));
        Mockito.when(modelMapper.map(this.userMockOne, UserDto.class))
                .thenReturn(this.userDtoMockOne);
        Mockito.when(modelMapper.map(this.userMockTwo, UserDto.class))
                .thenReturn(this.userDtoMockTwo);
        List<UserDto> response = userService.findAll(pageableMock);
        Assertions.assertEquals(sizeExpected, response.size());
        Assertions.assertEquals(List.of(this.userDtoMockOne, this.userDtoMockTwo), response);
        Mockito.verify(userRepository, Mockito.times(1)).findAll(pageableMock);
        Mockito.verify(modelMapper, Mockito.times(2)).map(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should throw exception when user don't exists by id when find one")
    void shouldThrowExceptionWhenUserNotExistsByIdWhenFindOne() {
        Long userIdToFind = 1L;
        String expectedMessage = "User not found with id: " + userIdToFind;
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        EntityNoSuchElementException ex = Assertions.assertThrows(EntityNoSuchElementException.class,
                () -> userService.findOne(userIdToFind, null));
        Assertions.assertEquals(ex.getMessage(), expectedMessage);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should find a user when it exists by id when find one")
    void shouldFindUserWhenExistsByIdWhenFindOne() {
        Long userIdToFind = 1L;

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userMockOne));
        Mockito.when(modelMapper.map(userMockOne, UserDto.class)).thenReturn(userDtoMockOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockOne);

        UserDto userDto = userService.findOne(userIdToFind, userDetails);

        Assertions.assertEquals(userDto, userDtoMockOne);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when a user wants to register with and existing username")
    void shouldThrowExceptionWhenUserWantRegisterWithExistingUsername() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " is already exists with name: " + this.registerDtoMock.getUsername();


        Mockito.when(this.userRepository.existsByName(Mockito.anyString())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                Assertions.assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.userService.register(this.registerDtoMock));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.userRepository).existsByName(this.registerDtoMock.getUsername());
    }

    @Test
    @DisplayName("Should register an user")
    void shouldRegisterUser() {
        ApiResponseDto expected = ApiResponseDataTestUtils
                .getMockRoleResponse("registered successfully", User.class);
        Role roleMock = RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_USER);

        Mockito.when(this.userRepository.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(this.modelMapper.map(this.registerDtoMock, User.class)).thenReturn(this.userMockOne);
        Mockito.when(this.roleRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(roleMock));
        Mockito.when(this.userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(this.userMockOne);

        ApiResponseDto response = this.userService.register(this.registerDtoMock);

        Assertions.assertEquals(expected.getMessage(), response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        Mockito.verify(this.userRepository).existsByName(this.registerDtoMock.getUsername());
        Mockito.verify(this.modelMapper).map(this.registerDtoMock, User.class);
        Mockito.verify(this.roleRepository).findByName(roleMock.getName());
        Mockito.verify(this.userRepository).save(this.userMockOne);
    }

    @Test
    @DisplayName("Should throw an exception when username is already exits")
    void shouldThrowExceptionWhenUsernameAlreadyExits() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " is already exists with name: " + this.userDtoMockOne.getUsername();

        Mockito.when(this.userRepository.existsByName(Mockito.anyString())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                Assertions.assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.userService.create(this.userDtoMockOne));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.userRepository).existsByName(this.userDtoMockOne.getUsername());
    }

    @Test
    @DisplayName("Should create an user")
    void shouldCreateUser() {
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " created successfully by admin";
        Mockito.when(this.modelMapper.map(this.userDtoMockOne, User.class)).thenReturn(this.userMockOne);
        Mockito.when(this.userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(this.userMockOne);
        Mockito.when(this.modelMapper.map(this.userMockOne, UserDto.class)).thenReturn(this.userDtoMockOne);

        UserDto userSaved = this.userService.create(this.userDtoMockOne);

        Assertions.assertEquals(expected, userSaved.getMessage());

        Mockito.verify(this.modelMapper).map(this.userDtoMockOne, User.class);
        Mockito.verify(this.userRepository).save(this.userMockOne);
        Mockito.verify(this.modelMapper).map(this.userMockOne, UserDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when user is not found by id when updating")
    void shouldThrowExceptionWhenUserIsNotFoundById() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " not found with id: " + this.userDtoMockOne.getUserId();

        Mockito.when(this.userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                Assertions.assertThrows(EntityNoSuchElementException.class,
                        () -> this.userService.update(userIdToFind, null, null));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when unauthorized by user and userDetails id when updating")
    void shouldThrowExceptionWhenUnauthorized() {
        Long userIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.UPDATE.name() + " this profile";

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockTree);

        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userMockOne));

        UnauthorizedPermissionException ex =
                Assertions.assertThrows(UnauthorizedPermissionException.class,
                        () -> this.userService.update(userIdToFind, null, userDetails));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should update an user when have authorization")
    void shouldUpdateUserWhenHaveAuthorization() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " updated successfully";

        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.userMockOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockOne);

        ApiResponseDto response = this.userService.update(userIdToFind,
                this.userDtoMockToUpdate, userDetails);

        Assertions.assertEquals(expected, response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        Mockito.verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should update an user with roles when have authorization")
    void shouldUpdateUserWhenHaveAuthorizationWithRoles() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " updated successfully";

        Role roleMock = RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_ADMIN);
        this.userMockOne.setRoles(Set.of(roleMock));
        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.userMockOne));
        Mockito.when(this.roleRepository.findByName(ArgumentMatchers.any(String.class)))
                .thenReturn(Optional.of(roleMock));

        Role roleMockAdmin = RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_ADMIN);
        this.userMockTwo.setRoles(Set.of(roleMockAdmin));
        AuthUserDetails userDetails = new AuthUserDetails(this.userMockTwo);

        RoleDto roleDtoMock = RoleServiceDataTestUtils.getMockRoleDto(1L, RoleName.ROLE_ADMIN);
        this.userDtoMockToUpdate.setRoles(Set.of(roleDtoMock));
        ApiResponseDto response = this.userService.update(userIdToFind,
                this.userDtoMockToUpdate, userDetails);

        Assertions.assertEquals(expected, response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        Mockito.verify(this.userRepository).findById(userIdToFind);
        Mockito.verify(this.roleRepository).findByName(roleDtoMock.getName().name());
    }

    @Test
    @DisplayName("Should throw an exception when user not found when deleting")
    void shouldThrowExceptionWhenUserNotFoundWhenDeleting() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " not found with id: " + userIdToFind;

        Mockito.when(this.userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                Assertions.assertThrows(EntityNoSuchElementException.class,
                        () -> this.userService.delete(userIdToFind, null));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized when deleting")
    void shouldThrowExceptionWhenUserIsUnauthorizedWhenDeleting() {
        Long userIdToFind = 1L;
        String expected = "You don't have the permission to "
                + MessageAction.DELETE.name() + " this profile";

        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.userMockOne));

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockTwo);

        UnauthorizedPermissionException ex =
                Assertions.assertThrows(UnauthorizedPermissionException.class,
                        () -> this.userService.delete(userIdToFind, userDetails));

        Assertions.assertEquals(expected, ex.getMessage());

        Mockito.verify(this.userRepository).findById(userIdToFind);
    }

    @Test
    @DisplayName("Should delete an user when deleting")
    void shouldDeleteUserWhenDeleting() {
        Long userIdToFind = 1L;
        String expected = GeneralUtil.simpleNameClass(User.class)
                + " deleted successfully";

        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.userMockOne));
        Mockito.doNothing().when(this.userRepository).delete(this.userMockOne);

        AuthUserDetails userDetails = new AuthUserDetails(this.userMockOne);

        ApiResponseDto response = this.userService.delete(userIdToFind, userDetails);

        Assertions.assertEquals(expected, response.getMessage());
        Assertions.assertTrue(response.getSuccess());

        Mockito.verify(this.userRepository).findById(userIdToFind);
        Mockito.verify(this.userRepository).delete(this.userMockOne);
    }

}