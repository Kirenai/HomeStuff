package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role roleUser;
    private Role roleMod;
    private Role roleAdmin;

    @BeforeEach
    void init() {
        RoleName roleNameOne = RoleName.ROLE_USER;
        RoleName roleNameTwo = RoleName.ROLE_MODERATOR;
        RoleName roleNameThree = RoleName.ROLE_ADMIN;

        this.roleUser = RoleServiceDataTestUtils.getMockRole(roleNameOne);
        this.roleMod = RoleServiceDataTestUtils.getMockRole(roleNameTwo);
        this.roleAdmin = RoleServiceDataTestUtils.getMockRole(roleNameThree);
    }

    @Test
    @DisplayName("Should find role by name")
    void shouldFindRoleByName() {
        String name = this.roleRepository.save(this.roleUser).getName();

        Optional<Role> roleFound = this.roleRepository.findByName(name);

        assertTrue(roleFound.isPresent());
        assertEquals(name, roleFound.get().getName());
    }

    @Test
    @DisplayName("Should check if role exists when exists by name")
    void shouldCheckIfRoleExistsWhenExistsByName() {
        String name = this.roleRepository.save(this.roleUser).getName();

        boolean roleExists = this.roleRepository.existsByName(name);

        assertTrue(roleExists);
    }

    @Test
    @DisplayName("Should find a list of roles when find all")
    void shouldFindAListOfRolesWhenFindAll() {
        Pageable pageableMock = Mockito.mock(Pageable.class);

        int size = this.roleRepository
                .saveAll(List.of(this.roleUser, this.roleMod, this.roleAdmin))
                .size();

        Page<Role> rolesFound = this.roleRepository.findAll(pageableMock);

        assertNotNull(rolesFound);
        assertEquals(size, rolesFound.getContent().size());
    }

}