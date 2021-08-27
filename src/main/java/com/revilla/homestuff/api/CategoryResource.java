package com.revilla.homestuff.api;

import java.util.List;
import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.service.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

/**
 * CategoryResource
 * @author Kirenai
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Qualifier("category.service")
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(Pageable pageable) {
        List<CategoryDto> response = this.categoryService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Long categoryId) {
        CategoryDto response = this.categoryService.findOne(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto) {
        CategoryDto response = this.categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
