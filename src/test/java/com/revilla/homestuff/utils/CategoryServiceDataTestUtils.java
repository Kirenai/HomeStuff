package com.revilla.homestuff.utils;

import com.revilla.homestuff.entity.Category;

/**
 * @author Kirenai
 */
public class CategoryServiceDataTestUtils {

    public static Category getCategoryMock(Long categoryId, String categoryName) {
        return Category.builder()
                .categoryId(categoryId)
                .name(categoryName)
                .build();
    }

}
