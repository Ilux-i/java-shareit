package ru.practicum.shareit.user;

public interface UserService {

    // Добавление User
    User add(User user);

    // Редактирование User
    User update(Long userId, User user);

    // Получение User по его id
    User getOne(Long userId);

    // Удаление User
    void delete(Long userId);

    void contains(Long userId);
}
