package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * NourishmentRepository
 * @author Kirenai
 */
@Repository
public interface NourishmentRepository extends JpaRepository<Nourishment, Long>, ExitsByProperty {

    @Override
    Boolean existsByName(String name);

    List<Nourishment> findByIsAvailable(Boolean isAvailable);

}
