package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.CurrentUser;
import com.revilla.homestuff.service.NourishmentService;
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

import java.util.List;

/**
 * NourishmentResource
 * @author Kirenai
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nourishments")
public class NourishmentResource {

    @Qualifier("nourishment.service")
    private final NourishmentService nourishmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<NourishmentDto>> getNourishments(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults(value = {
                    @SortDefault(sort = "nourishmentId", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        List<NourishmentDto> response = this.nourishmentService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{nourishmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<NourishmentDto> getNourishment(
            @PathVariable Long nourishmentId,
            @CurrentUser AuthUserDetails userDetails
    ) {
        NourishmentDto response = this.nourishmentService.findOne(nourishmentId,
                userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user/{userId}/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<NourishmentDto> createNourishment(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @RequestBody NourishmentDto nourishmentDto
    ) {
        NourishmentDto response = this.nourishmentService.create(
                userId,
                categoryId,
                nourishmentDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{nourishmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponseDto> updateNourishment(
            @PathVariable Long nourishmentId,
            @RequestBody NourishmentDto nourishmentDto,
            @CurrentUser AuthUserDetails userDetails
    ) {
        ApiResponseDto response = this.nourishmentService.update(nourishmentId,
                nourishmentDto, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{nourishmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponseDto> deleteNourishment(
            @PathVariable Long nourishmentId,
            @CurrentUser AuthUserDetails userDetails) {
        ApiResponseDto response = this.nourishmentService.delete(nourishmentId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
