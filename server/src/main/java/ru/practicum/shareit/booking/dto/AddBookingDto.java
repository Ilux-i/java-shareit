package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddBookingDto {
    private Long itemId;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private StatusBooking status = StatusBooking.WAITING;

    public AddBookingDto(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
