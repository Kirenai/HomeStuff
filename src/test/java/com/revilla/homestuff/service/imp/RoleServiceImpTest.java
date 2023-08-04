package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.mapper.role.RoleMapper;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import com.revilla.homestuff.utils.dto.response.ApiResponseDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImpTest {

    @InjectMocks
    private RoleServiceImp roleService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleMapper roleMapper;

    private final Long roleId = 1L;
    private final Long userId = 1L;
    private final String username = "kirenai";
    private final String password = "kirenai";
    private final String firstName = "kirenai";
    private final String lastName = "kirenai";
    private final Byte age = 22;

    private User userOne;
    private Role roleUser;
    private Role roleAdmin;
    private RoleDto roleUserDto;
    private RoleDto roleAdminDto;

    @BeforeEach
    void setUp() {
        Long userIdOne = 1L;
        String usernameOne = "kirenai";
        String passwordOne = "kirenai";
        String firstNameOne = "kirenai";
        String lastNameOne = "kirenai";
        Byte ageOne = 22;

        Long roleIdOne = 1L;
        Long roleIdTwo = 2L;

        this.userOne = UserServiceDataTestUtils.getUserMock(userIdOne, usernameOne,
                passwordOne, firstNameOne, lastNameOne, ageOne);
        this.roleUser = RoleServiceDataTestUtils.getRoleMock(roleIdOne, RoleName.ROLE_USER);
        this.roleAdmin = RoleServiceDataTestUtils.getRoleMock(roleIdTwo, RoleName.ROLE_ADMIN);
        this.roleUserDto = RoleServiceDataTestUtils.getRoleDtoMock(roleIdOne, RoleName.ROLE_USER);
        this.roleAdminDto = RoleServiceDataTestUtils.getRoleDtoMock(roleIdTwo, RoleName.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Should find a list of roles")
    void shouldFindListOfRoles() {
        int expectedSize = 2;
        Pageable pageableMock = mock(Pageable.class);

        when(this.roleRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(this.roleUser, this.roleAdmin)));
        when(this.roleMapper.mapOut(any()))
                .thenReturn(this.roleUserDto, this.roleAdminDto);

        List<RoleDto> response = this.roleService.findAll(pageableMock);

        assertNotNull(response);
        assertEquals(expectedSize, response.size());

        verify(this.roleRepository, times(1))
                .findAll(pageableMock);
        verify(this.roleMapper, times(2))
                .mapOut(any());
    }

    @Test
    @DisplayName("Should throw exception when role isn't found by id when finding one")
    void shouldThrowExceptionWhenRoleNotFoundByIdWhenFindingOne() {
        Long roleIdToFind = 1L;
        String messageExpected = "Role not found with id: " + roleIdToFind;

        when(this.roleRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> this.roleService.findOne(roleIdToFind, null)
        );

        assertEquals(messageExpected, ex.getMessage());

        verify(this.roleRepository).findById(roleIdToFind);
    }

    @Test
    @DisplayName("Should throw exception when user hasn't authorization when finding one")
    void shouldThrowExceptionWhenUserHasNotAuthorizationWhenFindingOne() {
        Long roleIdToFind = 1L;
        String messageExpected = "You don't have the permission to "
                + MessageAction.ACCESS.name() + " this role";


        when(this.roleRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Role()));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        UnauthorizedPermissionException ex = assertThrows(UnauthorizedPermissionException.class,
                () -> this.roleService.findOne(roleIdToFind, userDetails)
        );

        assertEquals(messageExpected, ex.getMessage());

        verify(this.roleRepository).findById(roleIdToFind);
    }

    @Test
    @DisplayName("Should find a role when finding one")
    void shouldFindRoleWhenFindingOne() {
        Long roleIdToFind = 1L;

        when(this.roleRepository.findById(anyLong()))
                .thenReturn(Optional.of(this.roleUser));
        when(this.roleMapper.mapOut(any()))
                .thenReturn(this.roleUserDto);

        this.userOne.setRoles(Set.of(this.roleAdmin));
        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        RoleDto response = this.roleService.findOne(roleIdToFind, userDetails);

        assertEquals(this.roleUserDto, response);

        verify(this.roleRepository).findById(roleIdToFind);
        verify(this.roleMapper).mapOut(any());
    }

    @Test
    @DisplayName("Should throw an exception when role name is duplicated when creating")
    void shouldThrowExceptionWhenRoleNameIsDuplicated() {
        RoleDto roleDto = RoleServiceDataTestUtils.getRoleDtoMock(1L, RoleName.ROLE_USER);

        when(this.roleRepository.existsByName(RoleName.ROLE_USER.name())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex = assertThrows(EntityDuplicateConstraintViolationException.class,
                () -> this.roleService.create(roleDto)
        );

        assertEquals(GeneralUtil.simpleNameClass(Role.class) + " is already exists with name: "
                        + roleDto.getName().name(),
                ex.getMessage()
        );

        verify(this.roleRepository).existsByName(RoleName.ROLE_USER.name());
    }

    @Test
    @DisplayName("Should create a role when creating")
    void shouldCreateRoleWhenCreating() {
        String expected = "ROLE_USER";
        Role role = RoleServiceDataTestUtils.getRoleMock(1L, RoleName.ROLE_USER);
        RoleDto roleDto = RoleServiceDataTestUtils.getRoleDtoMock(1L, RoleName.ROLE_USER);

        when(this.roleRepository.existsByName(RoleName.ROLE_USER.name())).thenReturn(false);
        when(this.roleMapper.mapIn(any())).thenReturn(role);
        when(this.roleRepository.save(any(Role.class))).thenReturn(role);
        when(this.roleMapper.mapOut(any())).thenReturn(roleDto);

        RoleDto roleCreated = this.roleService.create(roleDto);

        assertEquals(expected, roleCreated.getName().name());

        verify(this.roleRepository).existsByName(RoleName.ROLE_USER.name());
        verify(this.roleMapper, times(1)).mapIn(any());
        verify(this.roleRepository).save(role);
        verify(this.roleMapper, times(1)).mapOut(any());
    }

    @Test
    @DisplayName("Should throw an exception when a role by id is not found when updating")
    void shouldThrowExceptionWhenRoleByIdIsNotFound() {
        String expected = GeneralUtil.simpleNameClass(Role.class)
                + " not found with id: " + roleId;

        when(this.roleRepository.findById(roleId)).thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> this.roleService.update(roleId, new RoleDto()));

        assertEquals(expected, ex.getMessage());

        verify(this.roleRepository).findById(roleId);
    }

    // TODO: To implement this test, first implement the service
    @Test
    @DisplayName("Should update a Role when found by id")
    @Disabled
    void shouldUpdateARole() {
        Role role = RoleServiceDataTestUtils.getRoleMock(roleId, RoleName.ROLE_USER);
        when(this.roleRepository.findById(roleId)).thenReturn(Optional.of(role));
    }

    @Test
    @DisplayName("Should throw an exception when a role is not found by id when deleting")
    void shouldThrowExceptionWhenRoleIsNotFoundById() {
        String expected = GeneralUtil.simpleNameClass(Role.class)
                + " not found with id: " + roleId;
        when(this.roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                assertThrows(EntityNoSuchElementException.class,
                        () -> this.roleService.delete(roleId, null));

        assertEquals(expected, ex.getMessage());

        verify(this.roleRepository).findById(this.roleId);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized when deleting")
    void shouldThrowExceptionWhenUserIsUnauthorized() {
        String expected = "You don't have the permission to "
                + MessageAction.DELETE.name() + " this role";

        Role roleMock = RoleServiceDataTestUtils.getRoleMock(roleId,
                RoleName.ROLE_USER);
        User userMock = UserServiceDataTestUtils.getUserMock(userId,
                username, password, firstName, lastName, age);

        AuthUserDetails userDetails = new AuthUserDetails(userMock);

        when(this.roleRepository.findById(roleId))
                .thenReturn(Optional.of(roleMock));

        UnauthorizedPermissionException ex =
                assertThrows(UnauthorizedPermissionException.class,
                        () -> this.roleService.delete(roleId, userDetails));

        assertEquals(expected, ex.getMessage());

        verify(this.roleRepository).findById(this.roleId);
    }

    @Test
    @DisplayName("Should delete a role when user has authorization when deleting")
    void shouldDeleteRoleWhenUserHasAuthorizationWhenDeleting() {
        String messageAction = "deleted successfully";

        Role roleMock = RoleServiceDataTestUtils.getRoleMock(roleId,
                RoleName.ROLE_ADMIN);
        User userMock = UserServiceDataTestUtils.getUserMock(userId,
                username, password, firstName, lastName, age);
        userMock.setRoles(Set.of(roleMock));
        ApiResponseDto response = ApiResponseDataTestUtils
                .getApiResponseMock(messageAction, Role.class);

        AuthUserDetails userDetails = new AuthUserDetails(userMock);

        when(this.roleRepository.findById(roleId))
                .thenReturn(Optional.of(roleMock));
        doNothing().when(this.roleRepository).delete(any(Role.class));

        ApiResponseDto deleteResponse = this.roleService.delete(roleId, userDetails);

        assertEquals(response.getMessage(), deleteResponse.getMessage());
        assertTrue(deleteResponse.getSuccess());

        verify(this.roleRepository).findById(roleId);
        verify(this.roleRepository).delete(roleMock);
    }

}