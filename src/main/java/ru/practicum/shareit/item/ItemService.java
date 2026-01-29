package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.SearchItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    // Добавление Item
    public Item add(NewItemDto newItemDto);

    // Редактирование Item
    Item update(Long itemId, NewItemDto newItemDto);

    // Получение Item по его id
    Item getOne(GetItemDto getItemDto);

    // Получение Items по владельцу
    Collection<Item> getByOwner(Long id);

    Collection<Item> search(SearchItemDto searchItemDto);

}
