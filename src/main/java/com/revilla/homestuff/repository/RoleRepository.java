package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * RoleCategory
 * @author Kirenai
 */
public interface RoleRepository extends JpaRepository<Role, Long>, ExitsByProperty {

    Optional<Role> findByName(String name);

    @Override
    Boolean existsByName(String name);

}
