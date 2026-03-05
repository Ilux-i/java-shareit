package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchItemDto {
    private final Long ownerId;
    private final String text;

}
