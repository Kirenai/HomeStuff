package com.revilla.homestuff.api;

import java.util.List;
import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.service.ConsumptionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
 * ConsumptionResource
 * @author Kirenai
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/consumptions")
public class ConsumptionResource {

    @Qualifier("consumption.service")
    private final ConsumptionService consumptionService;

    @GetMapping
    public ResponseEntity<List<ConsumptionDto>> getNourishments(
            @PageableDefault(size = 3)
            @SortDefault.SortDefaults(value = {
                    @SortDefault(sort = "consumptionId", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        List<ConsumptionDto> response = this.consumptionService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{consumptionId}")
    public ResponseEntity<ConsumptionDto> getConsumption(
            @PathVariable Long consumptionId
    ) {
        ConsumptionDto response = this.consumptionService.findOne(consumptionId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/nourishment/{nourishmentId}/user/{userId}")
    public ResponseEntity<ConsumptionDto> createConsumption(
            @PathVariable Long nourishmentId,
            @PathVariable Long userId,
            @RequestBody ConsumptionDto consumptionDto) {
        ConsumptionDto response = this.consumptionService.create(nourishmentId, userId, consumptionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
