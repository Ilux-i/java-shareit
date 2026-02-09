package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Autowired
    private final ItemService itemService;

    // Добавление Item
    @PostMapping()
    public Item add(@RequestHeader(USER_ID_HEADER) Long userId,
                    @RequestBody ItemDto itemDto) {
        return itemService.add(new NewItemDto(userId, itemDto));
    }

    // Редактирование Item
    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader(USER_ID_HEADER) Long userId,
                       @PathVariable("itemId") Long itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, new NewItemDto(userId, itemDto));
    }

    // Получение Item по его id
    @GetMapping("/{itemId}")
    public Item getOne(@RequestHeader(USER_ID_HEADER) Long userId,
                       @PathVariable("itemId") Long itemId) {
        return itemService.getOne(new GetItemDto(userId, itemId));
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
