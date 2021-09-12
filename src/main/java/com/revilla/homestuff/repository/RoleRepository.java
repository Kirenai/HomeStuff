package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.util.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * RoleCategory
 * @author Kirenai
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

    Boolean existsByName(RoleName name);

}
