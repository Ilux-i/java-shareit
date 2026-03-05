package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Добавление User
    @PostMapping()
    public User add(@RequestBody User user) {
        return userService.add(new CreateUserDto(user));
    }

    // Редактирование User
    @PatchMapping("/{userId}")
    public User update(@PathVariable("userId") Long userId,
                       @RequestBody User user) {
        return userService.update(new UpdateUserDto(userId, user));
    }

    // Получение User по его id
    @GetMapping("/{userId}")
    public User getOne(@PathVariable("userId") Long userId) {
        return userService.getOne(userId);
    }

    // Удаление User
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        userService.delete(userId);
    }

}
