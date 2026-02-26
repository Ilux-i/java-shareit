package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private UserDto requestorId;
    private LocalDateTime created;
}
