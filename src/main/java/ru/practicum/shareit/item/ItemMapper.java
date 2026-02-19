package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class ItemMapper {
    public Item toItem(UpdateItemDto item, User owner) {
        Item res = new Item();
        res.setId(item.getId());
        res.setName(item.getName());
        res.setDescription(item.getDescription());
        res.setOwner(owner);
        res.setAvailable(item.getAvailable());
        res.setRequest(item.getRequest());

        return res;
    }

    public Item toItem(NewItemDto item, User owner) {
        Item res = new Item();
        res.setName(item.getName());
        res.setOwner(owner);
        res.setDescription(item.getDescription());
        res.setAvailable(item.getAvailable());
        res.setRequest(item.getRequest());

        return res;
    }

    public ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequest(item.getRequest());
        return dto;
    }
}