package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Nourishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * NourishmentRepository
 * @author Kirenai
 */
@Repository
public interface NourishmentRepository extends JpaRepository<Nourishment, Long> {
    
}
