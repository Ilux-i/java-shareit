package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {
    public Item toItem(UpdateItemDto item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getAvailable())
                .request(item.getRequest()).build();
    }

    public Item toItem(NewItemDto item) {
        return Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getAvailable())
                .request(item.getRequest()).build();
    }
}