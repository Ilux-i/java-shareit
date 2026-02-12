package ru.practicum.shareit.comment;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseCommentDto {
    private final Long id;
    private final String text;
    private final Item item;
    private final String authorName;
    private final LocalDateTime created;
}
