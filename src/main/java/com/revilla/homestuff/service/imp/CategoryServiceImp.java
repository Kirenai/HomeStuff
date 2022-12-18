package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.service.CategoryService;
import com.revilla.homestuff.util.ConstraintViolation;
import com.revilla.homestuff.util.GeneralUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CategoryService
 *
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("category.service")
public class CategoryServiceImp extends GeneralServiceImp<CategoryDto, Long, Category>
        implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public JpaRepository<Category, Long> getRepo() {
        return this.categoryRepository;
    }

    @Override
    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @Transactional
    @Override
    public CategoryDto create(CategoryDto data) {
        log.info("Calling the create method in " + getClass());
        ConstraintViolation.validateDuplicate(data.getName(),
                this.categoryRepository, Category.class);
        Category category = this.getModelMapper().map(data, super.getThirdGenericClass());
        Category categorySaved = this.categoryRepository.save(category);
        return this.getModelMapper().map(categorySaved, super.getFirstGenericClass());
    }

    @Transactional
    @Override
    public ApiResponseDto update(Long id, CategoryDto data) {
        log.info("Calling the update method in " + getClass());
        return this.categoryRepository.findById(id).map(c -> {
            c.setName(data.getName());
            return GeneralUtil.responseMessageAction(Category.class, "updated successfully");
        }).orElseThrow(() -> new EntityNoSuchElementException(
                GeneralUtil.simpleNameClass(Category.class) + " don't found with id: " + id));
    }

}
