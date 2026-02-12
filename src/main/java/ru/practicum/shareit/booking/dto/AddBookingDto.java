package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddBookingDto {
    private final Long itemId;
    private Long bookerId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final StatusBooking status = StatusBooking.WAITING;

    AddBookingDto(final Long itemId, final LocalDateTime start, final LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
