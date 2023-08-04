package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.mapper.GenericMapper;
import com.revilla.homestuff.mapper.category.CategoryMapper;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.service.CategoryService;
import com.revilla.homestuff.util.ConstraintViolation;
import com.revilla.homestuff.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CategoryService
 *
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImp extends GeneralServiceImp<CategoryDto, Long, Category>
        implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public JpaRepository<Category, Long> getRepo() {
        return this.categoryRepository;
    }

    @Override
    public GenericMapper<CategoryDto, Category> getMapper() {
        return this.categoryMapper;
    }

    @Transactional
    @Override
    public CategoryDto create(CategoryDto data) {
        log.info("Invoking CategoryServiceImp.create method");
        ConstraintViolation.validateDuplicate(data.getName(),
                this.categoryRepository, Category.class);
        Category category = this.categoryMapper.mapIn(data);
        Category categorySaved = this.categoryRepository.save(category);
        return this.categoryMapper.mapOut(categorySaved);
    }

    @Transactional
    @Override
    public ApiResponseDto update(Long id, CategoryDto data) {
        log.info("Invoking CategoryServiceImp.update method");
        return this.categoryRepository.findById(id)
                .map(c -> {
                    c.setName(data.getName());
                    return GeneralUtil.responseMessageAction(Category.class, "updated successfully");
                })
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(Category.class) + " don't found with id: " + id));
    }

}
