package com.revilla.homestuff.exception;

import com.revilla.homestuff.exception.unauthorize.UnauthorizedErrorMessage;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class UnauthorizedExceptionHandler {

    @ExceptionHandler(value = {UnauthorizedPermissionException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public UnauthorizedErrorMessage unauthorizedPermissionViolation(
            UnauthorizedPermissionException ex,
            WebRequest request) {
        log.error("Error: " + ex.getMessage());
        return new UnauthorizedErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

}
