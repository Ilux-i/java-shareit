package ru.practicum.shareit.user;

public interface UserStorage {

    // Добавление User
    User add(User user);

    // Редактирование User
    User update(User newUser, User oldUser);

    // Получение User по его id
    User findOne(Long userId);

    // Удаление User
    void delete(Long userId);

    void contains(Long userId);
}
