package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ConsumptionRepository
 * @author Kirenai
 */
@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    
}
