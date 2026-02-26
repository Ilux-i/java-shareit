package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.ApprovedBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingService service;

    @GetMapping("/{bookingId}")
    public Booking getBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                              @PathVariable("bookingId") Long bookingId) {
        return service.getBooking(new GetBookingDto(userId, bookingId));
    }

    @GetMapping()
    public Collection<Booking> getBookingsByState(@RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(value = "state", required = false) State state) {
        return service.getBookingsByState(new GetBookingsDto(userId, state));
    }

    @GetMapping("/owner")
    public Collection<Booking> getBookingsByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(value = "state", required = false) State state) {
        return service.getBookingsByOwner(new GetBookingsDto(userId, state));
    }

    @PostMapping()
    public Booking addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                              @RequestBody AddBookingDto dto) {
        dto.setBookerId(userId);
        return service.addBooking(dto);
    }

    @PatchMapping("/{bookingId}")
    public Booking approvedBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                   @PathVariable("bookingId") Long bookingId,
                                   @RequestParam(value = "approved") Boolean approved) {
        return service.approvedBooking(new ApprovedBookingDto(userId, bookingId, approved));
    }

}