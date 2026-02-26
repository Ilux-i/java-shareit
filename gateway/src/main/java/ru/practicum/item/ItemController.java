package ru.practicum.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.TextDto;
import ru.practicum.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemClient client;

    // Добавление Item
    @PostMapping()
    public ResponseEntity<Object> add(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody @Valid ItemDto itemDto
    ) {
        return client.add(userId, itemDto);
    }

    // Добавление комментариев
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody @Valid TextDto dto,
            @PositiveOrZero @PathVariable Long itemId
    ) {
        return client.addComment(userId, itemId, dto);
    }

    // Редактирование Item
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId,
            @PositiveOrZero @PathVariable("itemId") Long itemId,
            @RequestBody ItemDto dto
    ) {
        return client.update(userId, itemId, dto);
    }

    // Получение Item по его id
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getOne(
            @PositiveOrZero @PathVariable("itemId") Long itemId
    ) {
        return client.getOne(itemId);
    }

    // Получение Items по владельцу
    @GetMapping()
    public ResponseEntity<Object> getByOwner(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        return client.getByOwner(userId);
    }

    // Поиск Item
    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId,
            @NotNull @RequestParam(value = "text") String text
    ) {
        return client.search(userId, text);
    }

}
