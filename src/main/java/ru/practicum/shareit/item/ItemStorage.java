package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item add(NewItemDto newItemDto);

    Item update(UpdateItemDto updateItemDto);

    Item getOne(Long itemId);

    Collection<Item> getByOwner(Long id);

    Collection<Item> search(String text);

}
