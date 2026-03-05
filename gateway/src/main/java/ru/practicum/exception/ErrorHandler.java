package ru.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            MissingRequestHeaderException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final Exception e) {
        log.warn("Ошибка валидации данных: ", e);
        Map<String, String> response = new HashMap<>();
        response.put("error", "Ошибка валидации данных: " + e.getMessage());
        return response;
    }

    @ExceptionHandler
    public ErrorResponse handleOtherException(final Throwable e) {
        log.error("Unexpected exception: ", e);
        return ErrorResponse.create(
                e,
                HttpStatusCode.valueOf(500),
                "Непредвиденная ошибка: " + e.getMessage()
        );
    }
}