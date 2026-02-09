package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private Map<Long, Item> items = new HashMap<>();
    private Long counter = 0L;

    @Autowired
    private final UserService userService;

    @Override
    public Item add(NewItemDto newItemDto) {
        Item item = ItemMapper.toItem(newItemDto);
        valid(item);
        item.setId(getCounter());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(UpdateItemDto updateItemDto) {
        Item item = ItemMapper.toItem(updateItemDto);
        valid(item);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getOne(Long itemId) {
        contains(itemId);
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getByOwner(Long id) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(id))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Item> search(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        String searchText = text.trim().toLowerCase();

        return items.values().stream()
                // Item доступные для брони
                .filter(Item::getAvailable)
                // Item, имеющие у себя в названии или описании text
                .filter(item -> {
                    String name = item.getName();
                    String description = item.getDescription();

                    return name.toLowerCase().contains(searchText) ||
                            description.toLowerCase().contains(searchText);
                })
                .collect(Collectors.toSet());
    }

    private Long getCounter() {
        return counter++;
    }

    private void valid(Item item) {
        if (item.getName() == null ||
                item.getName().isEmpty() ||
                item.getDescription() == null ||
                item.getOwnerId() == null ||
                item.getAvailable() == null
        ) {
            throw new ValidationException("Invalid item");
        }
        userService.contains(item.getOwnerId());
    }

    private void contains(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ObjectNotFoundException("Item с id: " + itemId + " не найден");
        }
    }
}
