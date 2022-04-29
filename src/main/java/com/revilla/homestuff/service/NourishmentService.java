package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;

import java.util.List;

/**
 * NourishmentService
 *
 * @author Kirenai
 */
public interface NourishmentService extends GeneralService<NourishmentDto, Long> {

    List<NourishmentDto> findAllNourishmentByStatus(boolean isAvailable);

    NourishmentDto create(Long userId, Long CategoryId, NourishmentDto data, AuthUserDetails userDetails);

    ApiResponseDto update(Long id, NourishmentDto nourishmentDto, AuthUserDetails userDetails);

}
