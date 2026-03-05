package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovedBookingDto {
    private Long userId;
    private Long bookingId;
    private Boolean approved;

    public ApprovedBookingDto(final Long userId, final Long bookingId, final Boolean approved) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.approved = approved;
    }
}
