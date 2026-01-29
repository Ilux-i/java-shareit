package ru.practicum.shareit.item.dto;

import lombok.Getter;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.item.model.Item;

@Getter
public class UpdateItemDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Long ownerId;
    private final String request;
    private final Boolean available;

    public UpdateItemDto(NewItemDto newItemDto, Item oldItem) {
        this.id = oldItem.getId();
        if (!newItemDto.getOwnerId().equals(oldItem.getOwnerId()))
            throw new NoAccessException("Id владельца не совпадает");
        this.ownerId = oldItem.getOwnerId();

        // Заполнение новыми данными
        this.name = (newItemDto.getName() != null)
                ? newItemDto.getName()
                : oldItem.getName();
        this.description = (newItemDto.getDescription() != null)
                ? newItemDto.getDescription()
                : oldItem.getDescription();
        this.available = (newItemDto.getAvailable() != null)
                ? newItemDto.getAvailable()
                : oldItem.getAvailable();
        this.request = (newItemDto.getRequest() != null)
                ? newItemDto.getRequest()
                : oldItem.getRequest();
    }
}