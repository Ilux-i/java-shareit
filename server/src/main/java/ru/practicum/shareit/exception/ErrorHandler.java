package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentTypeMismatchException.class})
    public ErrorResponse handleValidationException(final Exception e) {
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleOtherException(final Throwable e) {
        log.error("Unexpected exception: ", e);
        Map<String, String> response = new HashMap<>();
        response.put("error", "Непредвиденная ошибка: " + e.getMessage());
        return response;
    }
}