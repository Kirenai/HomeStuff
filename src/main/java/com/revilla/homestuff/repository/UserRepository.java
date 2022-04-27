package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository
 * @author Kirenai
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, ExitsByProperty {

    Optional<User> findByUsername(String username);

    @Override
    @Query("select count(u)>0 from User u where u.username = ?1")
    Boolean existsByName(String name);

}
