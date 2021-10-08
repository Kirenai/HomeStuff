package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.service.RoleService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
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
    @DisplayName("Should not throw exception when role name is not duplicated and create role name")
    void shouldNotThrowExceptionWhenRoleNameIsNotDuplicatedAndCreateRoleName() {
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
    void create() {
    }

    @Test
    void update() {
    }
}