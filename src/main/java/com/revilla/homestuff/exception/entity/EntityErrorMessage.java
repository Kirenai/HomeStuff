package com.revilla.homestuff.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.revilla.homestuff.dto.response.ApiResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityErrorMessage {

    private Integer statusCode;
    private LocalDateTime timestamp;
    private ApiResponseDto response;
    private String description;

}
