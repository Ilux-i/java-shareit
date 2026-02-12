package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.ApprovedBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    Booking addBooking(AddBookingDto dto);

    Booking approvedBooking(ApprovedBookingDto dto);

    Booking getBooking(GetBookingDto dto);

    Collection<Booking> getBookingsByState(GetBookingsDto dto);

    Collection<Booking> getBookingsByOwner(GetBookingsDto dto);

}
