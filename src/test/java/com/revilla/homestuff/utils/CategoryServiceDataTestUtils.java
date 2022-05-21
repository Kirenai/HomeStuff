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

    public static List<CategoryDto> getCategoryDtoListMock() {
        return List.of(
                getCategoryDtoMock(1L, "Category 1"),
                getCategoryDtoMock(2L, "Category 2"),
                getCategoryDtoMock(3L, "Category 3")
        );
    }

}
