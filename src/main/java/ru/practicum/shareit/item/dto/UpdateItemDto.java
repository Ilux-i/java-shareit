package ru.practicum.shareit.item.dto;

import lombok.Getter;

@Getter
public class UpdateItemDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Long owner;
    private final String request;
    private final Boolean available;

    public UpdateItemDto(Long itemId, Long userId, ItemDto itemDto) {
        this.id = itemId;
        this.name = itemDto.getName();
        this.description = itemDto.getDescription();
        this.request = itemDto.getRequest();
        this.available = itemDto.getAvailable();
        this.owner = userId;
    }
}