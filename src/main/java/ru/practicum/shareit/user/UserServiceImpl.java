package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserStorage userStorage;

    @Override
    public User add(User user) {
        return userStorage.add(user);
    }

    @Override
    public User update(Long userId, User user) {
        User oldUser = userStorage.findOne(userId);

        user.setId(userId);
        if (user.getName() == null)
            user.setName(oldUser.getName());
        if (user.getEmail() == null)
            user.setEmail(oldUser.getEmail());

        return userStorage.update(user, oldUser);
    }

    @Override
    public User getOne(Long userId) {
        return userStorage.findOne(userId);
    }

    @Override
    public void delete(Long userId) {
        userStorage.delete(userId);
    }

    @Override
    public void contains(Long userId) {
        userStorage.contains(userId);
    }
}
