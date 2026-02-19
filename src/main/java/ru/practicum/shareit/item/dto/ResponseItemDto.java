package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseItemDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Boolean available;
    private String request;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<Comment> comments;

    public ResponseItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.owner = UserMapper.toUserDto(item.getOwner());
        this.available = item.getAvailable();
        this.request = item.getRequest();
    }
}
