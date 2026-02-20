package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.warn("Ошибка валидации данных: ", e);
        return ErrorResponse.create(
                e,
                HttpStatus.BAD_REQUEST,
                "Ошибка валидации данных: " + e.getMessage()
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorResponse handleDuplicateEmailException(final DuplicateEmailException e) {
        return ErrorResponse.create(
                e,
                HttpStatus.valueOf(409),
                e.getMessage()
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException e) {
        return ErrorResponse.create(
                e,
                HttpStatus.valueOf(404),
                e.getMessage()
        );
    }

    @ExceptionHandler(AccessRightsException.class)
    public ErrorResponse handleValidationException(final AccessRightsException e) {
        log.warn("Ошибка прав доступа: ", e);
        return ErrorResponse.create(
                e,
                HttpStatus.valueOf(403),
                "Ошибка прав доступа: " + e.getMessage()
        );
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