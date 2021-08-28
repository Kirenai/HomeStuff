package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.ConsumptionDto;

/**
 * ConsumptionService
 * @author Kirenai
 */
public interface ConsumptionService extends GeneralService<ConsumptionDto, Long> {

    ConsumptionDto create(Long nourishmentId, Long userId, ConsumptionDto data);

    ConsumptionDto update(Long id, ConsumptionDto categoryDto);

}

