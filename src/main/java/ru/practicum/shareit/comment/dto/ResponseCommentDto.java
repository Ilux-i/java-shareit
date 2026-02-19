package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseCommentDto {
    private final Long id;
    private final String text;
    private final ItemDto item;
    private final String authorName;
    private final LocalDateTime created;
}
