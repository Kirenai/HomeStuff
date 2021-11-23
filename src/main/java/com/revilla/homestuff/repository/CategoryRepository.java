package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CategoryRepository
 *
 * @author Kirenai
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String name);

}
