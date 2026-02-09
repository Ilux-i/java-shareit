package ru.practicum.shareit.item.dto;

import lombok.Getter;

@Getter
public class SearchItemDto {
    private final Long ownerId;
    private final String text;

    public SearchItemDto(Long ownerId, String text) {
        this.ownerId = ownerId;
        this.text = text;
    }
}
