package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseItemDto {
    private Long id;
    private String name;
    private String description;
    private User owner;
    private Boolean available;
    private String request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<Comment> comments;

    public ResponseItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.owner = item.getOwner();
        this.available = item.getAvailable();
        this.request = item.getRequest();
    }
}
