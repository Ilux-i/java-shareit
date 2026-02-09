package ru.practicum.shareit.exception;

// Ошибка валидации данных
public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
