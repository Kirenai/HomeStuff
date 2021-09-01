package com.revilla.homestuff.service.imp;

import javax.transaction.Transactional;
import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CategoryService
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

    @Transactional
	@Override
	public CategoryDto create(CategoryDto data) {
        log.info("Calling the create method in " + getClass());
        Category category = this.modelMapper.map(data, this.getThirdGenericClass());
        Category categorySaved = this.categoryRepository.save(category);
        return this.modelMapper.map(categorySaved, this.getFirstGenericClass());
	}

	@Override
	public CategoryDto update(Long id, CategoryDto data) {
        log.info("Calling the update method in " + getClass());
        return this.categoryRepository.findById(id)
            .map(c -> {
                c.setName(data.getName());
                return this.modelMapper.map(this.categoryRepository.save(c), CategoryDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
    }

}
