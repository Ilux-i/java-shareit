package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

public interface UserService {

    // Добавление User
    User add(CreateUserDto dto);

    // Редактирование User
    User update(UpdateUserDto dto);

    // Получение User по его id
    User getOne(Long userId);

    // Удаление User
    void delete(Long userId);

    void contains(Long userId);
}
