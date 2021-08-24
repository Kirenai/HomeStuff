package com.revilla.homestuff.service.imp;

import java.util.List;
import java.util.stream.Collectors;
import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.service.GeneralService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CategoryService
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("category.service")
public class CategoryService implements GeneralService<CategoryDto, Long> {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelmapper;

    @Override
	public List<CategoryDto> findAll(Pageable pageable) {
        log.info("Calling the findAll methond in CategoryService");
        return this.categoryRepository.findAll(pageable)
            .getContent()
            .stream()
            .map(c -> this.modelmapper.map(c, CategoryDto.class))
            .collect(Collectors.toList());
	}

	@Override
	public CategoryDto findOne(Long id) {
        log.info("Calling the findOne methond in CategoryService");
        Category category = this.categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
        return this.modelmapper.map(category, CategoryDto.class);
	}

	@Override
	public CategoryDto create(CategoryDto data) {
        log.info("Calling the create methond in CategoryService");
        Category category = this.modelmapper.map(data, Category.class);
        Category categorySaved = this.categoryRepository.save(category);
        return this.modelmapper.map(categorySaved, CategoryDto.class);
	}

	@Override
	public CategoryDto update(Long id, CategoryDto data) {
        log.info("Calling the update methond in CategoryService");
        return this.categoryRepository.findById(id)
            .map(c -> {
                c.setName(data.getName());
                return this.modelmapper.map(this.categoryRepository.save(c), CategoryDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
	}

	@Override
	public CategoryDto delete(Long id) {
        log.info("Calling the delete methond in CategoryService");
        return this.categoryRepository.findById(id)
            .map(c -> {
                this.categoryRepository.delete(c);
                return this.modelmapper.map(c, CategoryDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
	}

}
