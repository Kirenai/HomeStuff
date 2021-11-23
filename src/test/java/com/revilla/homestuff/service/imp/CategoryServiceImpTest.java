package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.service.CategoryService;
import com.revilla.homestuff.utils.CategoryServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * CategoryServiceImpTest
 *
 * @author Kirenai
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CategoryServiceImpTest {

    @Autowired
    private CategoryService categoryService;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ModelMapper modelMapper;

    private Category categoryOne;
    private CategoryDto categoryDtoOne;

    @BeforeEach
    void setUp() {
        Long categoryIdOne = 1L;
        String categoryNameOne = "Category One";

        this.categoryOne = CategoryServiceDataTestUtils
                .getCategoryMock(categoryIdOne, categoryNameOne);
        this.categoryDtoOne = CategoryServiceDataTestUtils
                .getCategoryDtoMock(categoryIdOne, categoryNameOne);
    }

    @Test
    @DisplayName("Should throw an exception when category name exists when create")
    void shouldThrowExceptionWhenCategoryNameExistsWhenCreate() {
        String expectedMessage = "Category is already exists with name: "
                + this.categoryDtoOne.getName();

        Mockito.when(this.categoryRepository.existsByName(Mockito.anyString()))
                .thenReturn(true);

        EntityDuplicateConstraintViolationException ex =
                assertThrows(EntityDuplicateConstraintViolationException.class,
                        () -> this.categoryService.create(this.categoryDtoOne)
                );

        assertEquals(expectedMessage, ex.getMessage());

        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .existsByName(this.categoryDtoOne.getName());
    }

    @Test
    @DisplayName("Should create a category when create")
    void shouldCreateCategoryWhenCreate() {
        Mockito.when(this.categoryRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.eq(Category.class)))
                .thenReturn(this.categoryOne);
        Mockito.when(this.categoryRepository.save(ArgumentMatchers.any(Category.class)))
                .thenReturn(this.categoryOne);
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.eq(CategoryDto.class)))
                .thenReturn(this.categoryDtoOne);

        CategoryDto response = this.categoryService.create(this.categoryDtoOne);

        assertEquals(this.categoryDtoOne, response);

        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .existsByName(this.categoryDtoOne.getName());
        Mockito.verify(this.modelMapper, Mockito.times(1))
                .map(this.categoryDtoOne, Category.class);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .save(this.categoryOne);
        Mockito.verify(this.modelMapper, Mockito.times(1))
                .map(this.categoryOne, CategoryDto.class);
    }

}