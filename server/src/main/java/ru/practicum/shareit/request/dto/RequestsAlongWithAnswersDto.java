package ru.practicum.shareit.request.dto;

import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemFromRequestDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RequestsAlongWithAnswersDto {
    private final Long id;
    private final String description;
    private final UserDto requestorId;
    private final LocalDateTime created;
    private final List<ItemFromRequestDto> items;

    public RequestsAlongWithAnswersDto(ItemRequest request, List<ItemFromRequestDto> items) {
        this.id = request.getId();
        this.description = request.getDescription();
        this.requestorId = UserMapper.toUserDto(request.getRequestor());
        this.created = request.getCreated();
        this.items = items;
    }
}
