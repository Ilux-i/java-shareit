package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

@Data
public class UpdateUserDto {
    private Long id;
    private String name;
    private String email;

    public UpdateUserDto(Long id, User user) {
        if (id == null)
            throw new ValidationException("Пустое значение userId");
        this.id = id;
        this.name = user.getName();
        this.email = user.getEmail();
    }

}
