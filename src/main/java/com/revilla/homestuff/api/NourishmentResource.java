package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.service.NourishmentService;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * NourishmentResource
 * @author Kirenai
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nourishments")
public class NourishmentResource {

    @Qualifier("nourishment.service")
    private final NourishmentService nourishmentService;

    @GetMapping("/{nourishmentId}")
    public ResponseEntity<NourishmentDto> getNourishment(@PathVariable Long nourishmentId) {
        NourishmentDto response = this.nourishmentService.findOne(nourishmentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<NourishmentDto> createNourishment(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @RequestBody NourishmentDto nourishmentDto) {
        NourishmentDto response = this.nourishmentService.create(
                userId,
                categoryId,
                nourishmentDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
