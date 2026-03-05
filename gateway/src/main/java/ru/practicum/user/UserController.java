package ru.practicum.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UpdateUserDto;
import ru.practicum.user.dto.UserDto;

// С @Controller падает запрос на удаление пользователя(не может найти статические ресурсы)
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient client;

    // Добавление User
    @PostMapping()
    public ResponseEntity<Object> add(
            @RequestBody @Valid UserDto dto) {
        return client.add(dto);
    }

    // Редактирование User
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @PositiveOrZero @PathVariable("userId") Long userId,
            @RequestBody @Valid UpdateUserDto dto) {
        return client.update(userId, dto);
    }

    // Получение User по его id
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOne(
            @PositiveOrZero @PathVariable("userId") Long userId) {
        return client.getUser(userId);
    }

    // Удаление User
    @DeleteMapping("/{userId}")
    public void delete(
            @PositiveOrZero @PathVariable("userId") Long userId) {
        client.delete(userId);
    }

}
