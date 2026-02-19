package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.NewCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.dto.TextDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    // Добавление Item
    @PostMapping()
    public Item add(@RequestHeader(USER_ID_HEADER) Long userId,
                    @RequestBody ItemDto itemDto) {
        return itemService.add(new NewItemDto(userId, itemDto));
    }

    // Добавление комментариев
    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @RequestBody TextDto dto,
                                         @PathVariable Long itemId) {
        return itemService.addComment(new NewCommentDto(userId, itemId, dto.getText()));
    }

    // Редактирование Item
    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader(USER_ID_HEADER) Long userId,
                       @PathVariable("itemId") Long itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(new UpdateItemDto(itemId, userId, itemDto));
    }

    // Получение Item по его id
    @GetMapping("/{itemId}")
    public ResponseItemDto getOne(@PathVariable("itemId") Long itemId) {
        return itemService.getFullById(itemId);
    }

    // Получение Items по владельцу
    @GetMapping()
    public Collection<Item> getByOwner(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getByOwner(userId);
    }

    // Поиск Item
    @GetMapping("/search")
    public Collection<Item> search(@RequestHeader(USER_ID_HEADER) Long userId,
                                   @RequestParam(value = "text") String text) {
        return itemService.search(new SearchItemDto(userId, text));
    }
}
