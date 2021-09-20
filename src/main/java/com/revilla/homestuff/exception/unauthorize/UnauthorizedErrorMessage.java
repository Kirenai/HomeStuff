package com.revilla.homestuff.exception.unauthorize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.revilla.homestuff.dto.response.ApiResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnauthorizedErrorMessage {

    private Integer statusCode;
    private LocalDateTime timestamp;
    private ApiResponseDto response;
    private String description;

}
