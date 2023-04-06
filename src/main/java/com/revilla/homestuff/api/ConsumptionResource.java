package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.CurrentUser;
import com.revilla.homestuff.service.ConsumptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConsumptionResource
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/consumptions")
public class ConsumptionResource {

    private final ConsumptionService consumptionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ConsumptionDto>> getConsumptions(
            @PageableDefault(size = 3)
            @SortDefault.SortDefaults(value = {
                    @SortDefault(sort = "consumptionId", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        log.info("Invoking ConsumptionResource.getConsumptions method");
        List<ConsumptionDto> response = this.consumptionService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{consumptionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ConsumptionDto> getConsumption(
            @PathVariable Long consumptionId,
            @CurrentUser AuthUserDetails userDetails
    ) {
        log.info("Invoking ConsumptionResource.getConsumption method");
        ConsumptionDto response = this.consumptionService.findOne(consumptionId,
                userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/nourishment/{nourishmentId}/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ConsumptionDto> createConsumption(
            @PathVariable Long nourishmentId,
            @PathVariable Long userId,
            @RequestBody ConsumptionDto consumptionDto,
            @CurrentUser AuthUserDetails userDetails) {
        log.info("Invoking ConsumptionResource.createConsumption method");
        ConsumptionDto response = this.consumptionService.create(nourishmentId,
                userId, consumptionDto, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
