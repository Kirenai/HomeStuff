package com.revilla.homestuff.api;

import java.util.List;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.CurrentUser;
import com.revilla.homestuff.service.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

/**
 * CategoryResource
 *
 * @author Kirenai
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Qualifier("category.service")
    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CategoryDto>> getCategories(
            @PageableDefault(size = 7)
            @SortDefault.SortDefaults(value = {
                    @SortDefault(sort = "categoryId", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        List<CategoryDto> response = this.categoryService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Long categoryId,
            @CurrentUser AuthUserDetails userDetails
    ) {
        CategoryDto response = this.categoryService.findOne(categoryId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto
    ) {
        CategoryDto response = this.categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updateUser(
            @PathVariable Long categoryId,
            @RequestBody CategoryDto categoryDto
    ) {
        ApiResponseDto response = this.categoryService.update(categoryId, categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
