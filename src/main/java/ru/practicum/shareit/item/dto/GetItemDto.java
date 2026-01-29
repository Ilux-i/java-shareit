package ru.practicum.shareit.item.dto;

import lombok.Getter;

@Getter
public class GetItemDto {
    private final Long ownerId;
    private final Long itemId;

    public GetItemDto(Long ownerId, Long iteId) {
        this.ownerId = ownerId;
        this.itemId = iteId;
    }
}
