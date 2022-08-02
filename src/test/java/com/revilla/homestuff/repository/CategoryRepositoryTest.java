package com.revilla.homestuff.repository;

import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.utils.CategoryServiceDataTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    private List<Category> categories;

    @BeforeEach
    void init() {
        this.categories = CategoryServiceDataTestUtils.getCategoryListMock();
        this.categoryRepository.saveAll(categories);
    }

    @Test
    @DisplayName("Should return true when call exists by name")
    void shouldReturnTrueWhenExistsByName() {
        String name = this.categories.get(0).getName();
        Boolean result = this.categoryRepository.existsByName(name);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when call exists by name")
    void shouldReturnFalseWhenExistsByName() {
        String name = "Random Name";
        Boolean result = this.categoryRepository.existsByName(name);

        Assertions.assertFalse(result);
    }

}
