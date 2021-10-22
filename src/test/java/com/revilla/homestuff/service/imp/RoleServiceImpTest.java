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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
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

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should throw exception when role name is duplicated")
    void shouldThrowExceptionWhenRoleNameIsDuplicated() {
        RoleDto roleDto = RoleServiceDataTestUtils.getMockRoleDto(1L, RoleName.ROLE_USER);

        Mockito.when(this.roleRepository.existsByName(RoleName.ROLE_USER)).thenReturn(true);

        EntityDuplicateConstraintViolationException ex = assertThrows(EntityDuplicateConstraintViolationException.class,
                () -> this.roleService.create(roleDto)
        );

        assertEquals(GeneralUtil.simpleNameClass(Role.class) + " is already exists with name: "
                        + roleDto.getName().name(),
                ex.getMessage()
        );

        Mockito.verify(this.roleRepository).existsByName(RoleName.ROLE_USER);
    }

    @Test
    @DisplayName("Should not throw exception when role name is not duplicated and create a role")
    void shouldNotThrowExceptionWhenRoleNameIsNotDuplicatedAndCreateRole() {
        String expected = "ROLE_USER";
        Role role = RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_USER);
        RoleDto roleDto = RoleServiceDataTestUtils.getMockRoleDto(1L, RoleName.ROLE_USER);

        Mockito.when(this.roleRepository.existsByName(RoleName.ROLE_USER)).thenReturn(false);
        Mockito.when(this.modelMapper.map(roleDto, Role.class)).thenReturn(role);
        Mockito.when(this.roleRepository.save(any(Role.class))).thenReturn(role);
        Mockito.when(this.modelMapper.map(role, RoleDto.class)).thenReturn(roleDto);

        RoleDto roleCreated = this.roleService.create(roleDto);

        assertEquals(expected, roleCreated.getName().name());

        Mockito.verify(this.roleRepository).existsByName(RoleName.ROLE_USER);
        Mockito.verify(this.modelMapper).map(roleDto, Role.class);
        Mockito.verify(this.roleRepository).save(role);
        Mockito.verify(this.modelMapper).map(role, RoleDto.class);
    }

    @Test
    @DisplayName("Should throw an exception when a role by id is not found")
    void shouldThrowExceptionWhenRoleByIdIsNotFound() {
        String expected = GeneralUtil.simpleNameClass(Role.class)
                + " don't found with id: " + roleId;

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
    @DisplayName("Should throw an exception when a role is not found by id")
    void shouldThrowExceptionWhenRoleIsNotFoundById() {
        String expected = GeneralUtil.simpleNameClass(Role.class)
                + " don't found with id: " + roleId;
        Mockito.when(this.roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        EntityNoSuchElementException ex =
                assertThrows(EntityNoSuchElementException.class,
                        () -> this.roleService.delete(roleId, null));

        assertEquals(expected, ex.getMessage());

        Mockito.verify(this.roleRepository).findById(this.roleId);
    }

    @Test
    @DisplayName("Should throw an exception when user is unauthorized")
    void shouldThrowExceptionWhenUserIsUnauthorized() {
        String expected = "You don't have the permission to "
                + MessageAction.ACCESS.name() + " this Role";

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
    @DisplayName("Should delete a role when a role is found by id and user have authorization")
    void shouldDeleteRoleWhenRoleIsFoundByIdAndUserHaveAuthorization() {
        String messageAction = "successfully removed";

        Role roleMock = RoleServiceDataTestUtils.getMockRole(roleId,
                RoleName.ROLE_ADMIN);
        User userMock = UserServiceDataTestUtils.getMockUser(userId,
                username, password, firstName, lastName, age);
        userMock.setRoles(Set.of(roleMock));
        ApiResponseDto response = ApiResponseDataTestUtils
                .getMockRoleResponse(messageAction, Role.class);

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