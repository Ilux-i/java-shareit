package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Long requestId;
    private Boolean available;
}
