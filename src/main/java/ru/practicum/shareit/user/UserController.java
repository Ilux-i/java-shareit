package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    // Добавление User
    @PostMapping()
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

    // Редактирование User
    @PatchMapping("/{userId}")
    public User update(@PathVariable("userId") Long userId,
                       @RequestBody User user) {
        return userService.update(userId, user);
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
