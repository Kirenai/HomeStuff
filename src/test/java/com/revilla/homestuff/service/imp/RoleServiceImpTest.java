package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.RoleService;
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class RoleServiceImpTest {

    @Autowired
    private RoleService roleService;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private ModelMapper modelMapper;

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

        this.userOne = UserServiceDataTestUtils.getMockUser(userIdOne, usernameOne,
                passwordOne, firstNameOne, lastNameOne, ageOne);
        this.roleUser = RoleServiceDataTestUtils.getMockRole(roleIdOne, RoleName.ROLE_USER);
        this.roleAdmin = RoleServiceDataTestUtils.getMockRole(roleIdTwo, RoleName.ROLE_ADMIN);
        this.roleUserDto = RoleServiceDataTestUtils.getMockRoleDto(roleIdOne, RoleName.ROLE_USER);
        this.roleAdminDto = RoleServiceDataTestUtils.getMockRoleDto(roleIdTwo, RoleName.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Should find a list of roles")
    void shouldFindListOfRoles() {
        int expectedSize = 2;
        Pageable pageableMock = Mockito.mock(Pageable.class);

        Mockito.when(this.roleRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(this.roleUser, this.roleAdmin)));
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.eq(RoleDto.class)))
                .thenReturn(this.roleUserDto, this.roleAdminDto);

        List<RoleDto> response = this.roleService.findAll(pageableMock);

        assertEquals(expectedSize, response.size());
        assertEquals(this.roleUserDto, response.get(0));
        assertEquals(this.roleAdminDto, response.get(1));

        Mockito.verify(this.roleRepository, Mockito.times(1))
                .findAll(pageableMock);
        Mockito.verify(this.modelMapper, Mockito.times(2))
                .map(Mockito.any(), Mockito.eq(RoleDto.class));
    }

    @Test
    @DisplayName("Should throw exception when role isn't found by id when finding one")
    void shouldThrowExceptionWhenRoleNotFoundByIdWhenFindingOne() {
        Long roleIdToFind = 1L;
        String messageExpected = "Role not found with id: " + roleIdToFind;

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> this.roleService.findOne(roleIdToFind, null)
        );

        assertEquals(messageExpected, ex.getMessage());

        Mockito.verify(this.roleRepository).findById(roleIdToFind);
    }

    @Test
    @DisplayName("Should throw exception when user hasn't authorization when finding one")
    void shouldThrowExceptionWhenUserHasNotAuthorizationWhenFindingOne() {
        Long roleIdToFind = 1L;
        String messageExpected = "You don't have the permission to "
                + MessageAction.ACCESS.name() + " this role";


        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Role()));

        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        UnauthorizedPermissionException ex = assertThrows(UnauthorizedPermissionException.class,
                () -> this.roleService.findOne(roleIdToFind, userDetails)
        );

        assertEquals(messageExpected, ex.getMessage());

        Mockito.verify(this.roleRepository).findById(roleIdToFind);
    }

    @Test
    @DisplayName("Should find a role when finding one")
    void shouldFindRoleWhenFindingOne() {
        Long roleIdToFind = 1L;

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.roleUser));
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.eq(RoleDto.class)))
                .thenReturn(this.roleUserDto);

        this.userOne.setRoles(Set.of(this.roleAdmin));
        AuthUserDetails userDetails = new AuthUserDetails(this.userOne);

        RoleDto response = this.roleService.findOne(roleIdToFind, userDetails);

        assertEquals(this.roleUserDto, response);

        Mockito.verify(this.roleRepository).findById(roleIdToFind);
        Mockito.verify(this.modelMapper).map(this.roleUser, RoleDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when role name is duplicated when creating")
    void shouldThrowExceptionWhenRoleNameIsDuplicated() {
        RoleDto roleDto = RoleServiceDataTestUtils.getMockRoleDto(1L, RoleName.ROLE_USER);

        Mockito.when(this.roleRepository.existsByName(RoleName.ROLE_USER.name())).thenReturn(true);

        EntityDuplicateConstraintViolationException ex = assertThrows(EntityDuplicateConstraintViolationException.class,
                () -> this.roleService.create(roleDto)
        );

        assertEquals(GeneralUtil.simpleNameClass(Role.class) + " is already exists with name: "
                        + roleDto.getName().name(),
                ex.getMessage()
        );

        Mockito.verify(this.roleRepository).existsByName(RoleName.ROLE_USER.name());
    }

    @Test
    @DisplayName("Should create a role when creating")
    void shouldCreateRoleWhenCreating() {
        String expected = "ROLE_USER";
        Role role = RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_USER);
        RoleDto roleDto = RoleServiceDataTestUtils.getMockRoleDto(1L, RoleName.ROLE_USER);

        Mockito.when(this.roleRepository.existsByName(RoleName.ROLE_USER.name())).thenReturn(false);
        Mockito.when(this.modelMapper.map(roleDto, Role.class)).thenReturn(role);
        Mockito.when(this.roleRepository.save(any(Role.class))).thenReturn(role);
        Mockito.when(this.modelMapper.map(role, RoleDto.class)).thenReturn(roleDto);

        RoleDto roleCreated = this.roleService.create(roleDto);

        assertEquals(expected, roleCreated.getName().name());

        Mockito.verify(this.roleRepository).existsByName(RoleName.ROLE_USER.name());
        Mockito.verify(this.modelMapper).map(roleDto, Role.class);
        Mockito.verify(this.roleRepository).save(role);
        Mockito.verify(this.modelMapper).map(role, RoleDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when a role by id is not found when updating")
    void shouldThrowExceptionWhenRoleByIdIsNotFound() {
        String expected = GeneralUtil.simpleNameClass(Role.class)
                + " not found with id: " + roleId;

        Mockito.when(this.roleRepository.findById(roleId)).thenReturn(Optional.empty());

        EntityNoSuchElementException ex = assertThrows(EntityNoSuchElementException.class,
                () -> this.roleService.update(roleId, new RoleDto()));

        assertEquals(expected, ex.getMessage());

        Mockito.verify(this.roleRepository).findById(roleId);
    }

    // TODO: To implement this test, first implement the service
    @Test
    @DisplayName("Should update a Role when found by id")
    @Disabled
    void shouldUpdateARole() {
        Role role = RoleServiceDataTestUtils.getMockRole(roleId, RoleName.ROLE_USER);
        Mockito.when(this.roleRepository.findById(roleId)).thenReturn(Optional.of(role));
    }

    @Test
    @DisplayName("Should throw an exception when a role is not found by id when deleting")
    void shouldThrowExceptionWhenRoleIsNotFoundById() {
        String expected = GeneralUtil.simpleNameClass(Role.class)
                + " not found with id: " + roleId;
        Mockito.when(this.roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                assertThrows(EntityNoSuchElementException.class,
                        () -> this.roleService.delete(roleId, null));

        assertEquals(expected, ex.getMessage());

        Mockito.verify(this.roleRepository).findById(this.roleId);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized when deleting")
    void shouldThrowExceptionWhenUserIsUnauthorized() {
        String expected = "You don't have the permission to "
                + MessageAction.DELETE.name() + " this role";

        Role roleMock = RoleServiceDataTestUtils.getMockRole(roleId,
                RoleName.ROLE_USER);
        User userMock = UserServiceDataTestUtils.getMockUser(userId,
                username, password, firstName, lastName, age);

        AuthUserDetails userDetails = new AuthUserDetails(userMock);

        Mockito.when(this.roleRepository.findById(roleId))
                .thenReturn(Optional.of(roleMock));

        UnauthorizedPermissionException ex =
                assertThrows(UnauthorizedPermissionException.class,
                        () -> this.roleService.delete(roleId, userDetails));

        assertEquals(expected, ex.getMessage());

        Mockito.verify(this.roleRepository).findById(this.roleId);
    }

    @Test
    @DisplayName("Should delete a role when user has authorization when deleting")
    void shouldDeleteRoleWhenUserHasAuthorizationWhenDeleting() {
        String messageAction = "deleted successfully";

        Role roleMock = RoleServiceDataTestUtils.getMockRole(roleId,
                RoleName.ROLE_ADMIN);
        User userMock = UserServiceDataTestUtils.getMockUser(userId,
                username, password, firstName, lastName, age);
        userMock.setRoles(Set.of(roleMock));
        ApiResponseDto response = ApiResponseDataTestUtils
                .getApiResponseMock(messageAction, Role.class);

        AuthUserDetails userDetails = new AuthUserDetails(userMock);

        Mockito.when(this.roleRepository.findById(roleId))
                .thenReturn(Optional.of(roleMock));
        Mockito.doNothing().when(this.roleRepository).delete(any(Role.class));

        ApiResponseDto deleteResponse = this.roleService.delete(roleId, userDetails);

        assertEquals(response.getMessage(), deleteResponse.getMessage());
        assertTrue(deleteResponse.getSuccess());

        Mockito.verify(this.roleRepository).findById(roleId);
        Mockito.verify(this.roleRepository).delete(roleMock);
    }

}