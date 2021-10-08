package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.service.RoleService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Long roleId = 1L;
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
        Long roleId = 1L;
        Role role = RoleServiceDataTestUtils.getMockRole(roleId, RoleName.ROLE_USER);
        Mockito.when(this.roleRepository.findById(roleId)).thenReturn(Optional.of(role));
    }
}