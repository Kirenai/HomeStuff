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

    private Role roleOne;
    private Role roleTwo;
    private Role roleThree;

    @BeforeEach
    void setUp() {
        Long roleIdOne = 1L;
        RoleName roleNameOne = RoleName.ROLE_USER;

        Long roleIdTwo = 2L;
        RoleName roleNameTwo = RoleName.ROLE_MODERATOR;

        Long roleIdThree = 3L;
        RoleName roleNameThree = RoleName.ROLE_ADMIN;

        this.roleOne = RoleServiceDataTestUtils.getMockRole(roleIdOne, roleNameOne);
        this.roleTwo = RoleServiceDataTestUtils.getMockRole(roleIdTwo, roleNameTwo);
        this.roleThree = RoleServiceDataTestUtils.getMockRole(roleIdThree, roleNameThree);
    }

    @Test
    @DisplayName("Should find role by name")
    void shouldFindRoleByName() {
        this.roleRepository.save(this.roleOne);

        Optional<Role> roleFound = roleRepository.findByName(this.roleOne.getName());

        assertTrue(roleFound.isPresent());
        assertEquals(this.roleOne.getName(), roleFound.get().getName());
    }

    @Test
    @DisplayName("Should check if role exists when exists by name")
    void shouldCheckIfRoleExistsWhenExistsByName() {
        boolean roleExists = roleRepository.existsByName(this.roleOne.getName());

        assertTrue(roleExists);
    }

    @Test
    @DisplayName("Should find a list of roles when find all")
    void shouldFindAListOfRolesWhenFindAll() {
        Long expectedSize = 3L;
        Pageable pageableMock = Mockito.mock(Pageable.class);

        this.roleRepository.saveAll(List.of(this.roleOne, this.roleTwo, this.roleThree));

        Page<Role> rolesFound = this.roleRepository.findAll(pageableMock);

        assertNotNull(rolesFound);
        assertEquals(expectedSize, rolesFound.getContent().size());
    }

}