package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemFromRequestDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Boolean available;
}
