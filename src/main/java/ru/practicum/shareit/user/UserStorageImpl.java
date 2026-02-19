package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserStorageImpl implements UserStorage {

    private static Map<Long, User> users = new HashMap<>();
    private static Long counter = 0L;

    @Override
    public User add(User user) {
        validEmail(user.getEmail());
        Long id = getCounter();
        user.setId(id);
        users.put(id, user);
        log.info("Пользователь с id: {} успешно сохранён", id);
        return user;
    }

    @Override
    public User update(User newUser, User oldUser) {
        users.remove(oldUser.getId());
        try {
            validEmail(newUser.getEmail());
        } catch (ValidationException e) {
            users.put(oldUser.getId(), oldUser);
            throw e;
        }
        users.put(oldUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User findOne(Long userId) {
        contains(userId);
        return users.get(userId);
    }

    @Override
    public void delete(Long userId) {
        contains(userId);
        users.remove(userId);
        log.info("Пользователь с id: {} успешно удалён", userId);
    }

    private Long getCounter() {
        return counter++;
    }

    private void validEmail(String email) {
        if (email == null ||
                !email.contains("@") ||
                users.values()
                        .stream()
                        .map(User::getEmail)
                        .toList()
                        .contains(email)) {
            log.warn("{} - этот email уже используется", email);
            throw new ValidationException("Электронная почта уже существует");
        }
    }

    @Override
    public void contains(Long userId) {
        if (userId == null ||
                !users.containsKey(userId)) {
            log.warn("Пользователь с id: {} не найден", userId);
        }
    }
}
