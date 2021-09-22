package com.revilla.homestuff.exception;

import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.exception.entity.EntityDuplicateConstraintViolationException;
import com.revilla.homestuff.exception.entity.EntityErrorMessage;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler(value = {EntityNoSuchElementException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public EntityErrorMessage entityNoSuchElement(EntityNoSuchElementException ex,
                                                  WebRequest request) {
        log.error("Error: " + ex.getMessage());
        return new EntityErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                new ApiResponseDto(Boolean.FALSE, ex.getMessage()),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = {EntityDuplicateConstraintViolationException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public EntityErrorMessage entityDuplicateConstraintViolation(
            EntityDuplicateConstraintViolationException ex,
            WebRequest request) {
        log.error("Error: " + ex.getMessage());
        return new EntityErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                new ApiResponseDto(Boolean.FALSE, ex.getMessage()),
                request.getDescription(false)
        );
    }

}
