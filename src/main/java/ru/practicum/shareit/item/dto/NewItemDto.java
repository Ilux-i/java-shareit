package ru.practicum.shareit.item.dto;

import lombok.Getter;

@Getter
public class NewItemDto {
    private final String name;
    private final String description;
    private final Long ownerId;
    private final String request;
    private final Boolean available;

    public NewItemDto(Long ownerId, ItemDto itemDto) {
        this.name = itemDto.getName();
        this.description = itemDto.getDescription();
        this.ownerId = ownerId;
        this.request = itemDto.getRequest();
        this.available = itemDto.getAvailable();
    }
}
