package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

@UtilityClass
public class RequestMapper {

    public ItemRequestDto toRequestDto(final ItemRequest request) {
        if (request == null) return null;
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                UserMapper.toUserDto(request.getRequestor()),
                request.getCreated()
        );
    }

    public ItemRequestDto toRequestDto(final ItemRequest request, User user) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                UserMapper.toUserDto(user),
                request.getCreated()
        );
    }

}
