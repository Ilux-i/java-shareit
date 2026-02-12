package ru.practicum.shareit.booking.dto;

import lombok.Getter;

@Getter
public class ApprovedBookingDto {
    private final Long userId;
    private final Long bookingId;
    private final Boolean approved;

    public ApprovedBookingDto(final Long userId, final Long bookingId, final Boolean approved) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.approved = approved;
    }
}
