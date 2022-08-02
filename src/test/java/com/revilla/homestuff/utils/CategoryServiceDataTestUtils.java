package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.entity.Category;

import java.util.List;

/**
 * @author Kirenai
 */
public class CategoryServiceDataTestUtils {

    public static Category getCategoryMock(String categoryName) {
        return getCategoryMock(null, categoryName);
    }

    public static Category getCategoryMock(Long categoryId, String categoryName) {
        return Category.builder()
                .categoryId(categoryId)
                .name(categoryName)
                .build();
    }

    public static CategoryDto getCategoryDtoMock(Long categoryId, String categoryName) {
        return CategoryDto.builder()
                .categoryId(categoryId)
                .name(categoryName)
                .build();
    }

    public static List<Category> getCategoryListMock() {
        return List.of(
                getCategoryMock(1L, "Category 1"),
                getCategoryMock(2L, "Category 2"),
                getCategoryMock(3L, "Category 3"),
                getCategoryMock(4L, "Category 4"),
                getCategoryMock(5L, "Category 5"),
                getCategoryMock(6L, "Category 6"),
                getCategoryMock(7L, "Category 7")
        );
    }

    public static List<CategoryDto> getCategoryDtoListMock() {
        return List.of(
                getCategoryDtoMock(1L, "Category 1"),
                getCategoryDtoMock(2L, "Category 2"),
                getCategoryDtoMock(3L, "Category 3")
        );
    }

}
