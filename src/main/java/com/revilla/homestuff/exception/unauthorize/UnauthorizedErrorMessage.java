package com.revilla.homestuff.exception.unauthorize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnauthorizedErrorMessage {

    private Integer statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;

}
