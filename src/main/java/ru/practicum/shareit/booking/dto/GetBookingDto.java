package ru.practicum.shareit.booking.dto;

import lombok.Getter;

@Getter
public class GetBookingDto {
    private final Long userId;
    private final Long bookingId;

    public GetBookingDto(Long userId, Long bookingId) {
        this.userId = userId;
        this.bookingId = bookingId;
    }
}
