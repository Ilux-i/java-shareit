package ru.practicum.shareit.user.dto;

import lombok.Getter;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

@Getter
public class CreateUserDto {
    private final String name;
    private final String email;

    public CreateUserDto(User user) {
        String name = user.getName();
        String email = user.getEmail();
        if (name == null ||
                name.isEmpty())
            throw new ValidationException("Пустое значение email");
        if (email == null ||
                email.isEmpty())
            throw new ValidationException("Пустое значение email");
        this.name = name;
        this.email = user.getEmail();
    }
}
