package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;

/**
 * CategoryService
 * @author Kirenai
 */
public interface CategoryService extends GeneralService<CategoryDto, Long> {

    CategoryDto create(CategoryDto data);

    ApiResponseDto update(Long id, CategoryDto categoryDto);

}
