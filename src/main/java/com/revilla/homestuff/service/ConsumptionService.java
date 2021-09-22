package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.security.AuthUserDetails;

/**
 * ConsumptionService
 * @author Kirenai
 */
public interface ConsumptionService extends GeneralService<ConsumptionDto, Long> {

    ConsumptionDto create(Long nourishmentId, Long userId, ConsumptionDto data, AuthUserDetails userDetails);

}

