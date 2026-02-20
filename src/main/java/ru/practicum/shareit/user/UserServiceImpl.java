package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository rep;

    @Override
    public User add(CreateUserDto dto) {
        // Валидация email
        validEmail(dto.getEmail());
        return rep.save(UserMapper.toUser(dto));
    }

    @Override
    @Transactional
    public User update(UpdateUserDto dto) {
        // Обновление данных с помощью старого пользователя
        User oldUser = getOne(dto.getId());
        if (dto.getName() == null || dto.getName().isEmpty()) dto.setName(oldUser.getName());
        if (dto.getEmail() == null || dto.getEmail().isEmpty())
            dto.setEmail(oldUser.getEmail());
        else
            validEmail(dto.getEmail());
        User newUser = UserMapper.toUser(dto);
        // Обновление пользователя
        rep.save(newUser);
        return newUser;
    }

    @Override
    public User getOne(Long userId) {
        return rep.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
    }

    @Override
    public void delete(Long userId) {
        contains(userId);
        rep.deleteById(userId);
    }

    @Override
    public void contains(Long userId) {
        if (!rep.existsById(userId)) throw new ObjectNotFoundException("Пользователь не найден");
    }

    private void validEmail(String email) {
        if (!email.contains("@")) throw new ValidationException("Ошибка валидации email");
        if (rep.existsByEmail(email)) throw new DuplicateEmailException("Данный email уже существует");
    }
}
