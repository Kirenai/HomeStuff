package com.revilla.homestuff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponseDto
 *
 * @author Kirenai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

    private Boolean success;
    private String message;

}
