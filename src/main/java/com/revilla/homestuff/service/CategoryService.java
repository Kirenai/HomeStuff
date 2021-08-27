package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.CategoryDto;

/**
 * CategoryService
 * @author Kirenai
 */
public interface CategoryService extends GeneralService<CategoryDto, Long> {

    CategoryDto create(CategoryDto data);

    CategoryDto update(Long id, CategoryDto categoryDto);

}
