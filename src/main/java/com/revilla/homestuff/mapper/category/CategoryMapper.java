package com.revilla.homestuff.mapper.category;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends GenericMapper<CategoryDto, Category> {

    @Override
    Category mapIn(CategoryDto dto);

    @Override
    CategoryDto mapOut(Category input);
}
