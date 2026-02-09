package ru.practicum.shareit.exception;

// Не найден объект
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
