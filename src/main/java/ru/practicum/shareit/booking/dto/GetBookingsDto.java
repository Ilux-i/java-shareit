package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.State;

@Getter
@Setter
public class GetBookingsDto {
    private final Long userId;
    private State state;

    public GetBookingsDto(Long userId, State state) {
        this.userId = userId;
        this.state = state;
    }
}
