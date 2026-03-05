package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetBookingDto {
    private Long userId;
    private Long bookingId;

    public GetBookingDto(Long userId, Long bookingId) {
        this.userId = userId;
        this.bookingId = bookingId;
    }
}
