package ru.practicum.shareit.comment.dto;

import lombok.Getter;

@Getter
public class NewCommentDto {
    private final Long userId;
    private final Long itemId;
    private final String text;

    public NewCommentDto(final Long userId, final Long itemId, final String text) {
        this.userId = userId;
        this.itemId = itemId;
        this.text = text;
    }
}
