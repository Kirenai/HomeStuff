package com.revilla.homestuff.service;

import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.security.AuthUserDetails;

/**
 * NourishmentService
 * @author Kirenai
 */
public interface NourishmentService extends GeneralService<NourishmentDto, Long> {

    NourishmentDto create(Long userId, Long CategoryId,  NourishmentDto data);

    NourishmentDto update(Long id, NourishmentDto nourishmentDto, AuthUserDetails userDetails);

}
