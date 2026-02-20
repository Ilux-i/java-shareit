package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.ApprovedBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository rep;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking addBooking(AddBookingDto dto) {
        // Верификация
        if (dto.getItemId() == null ||
                dto.getBookerId() == null ||
                dto.getStart() == null ||
                dto.getEnd() == null)
            throw new ValidationException("Отсутствие части, либо всех начальных данных");
        if (dto.getStart().isAfter(dto.getEnd()) ||
                dto.getStart().isEqual(dto.getEnd())
        )
            throw new ValidationException("Передан невозможный временной отрезок");
        // Проверка на существование бронирующего
        userService.contains(dto.getBookerId());
        Item item = itemService.getOne(dto.getItemId());
        Booking booking = BookingMapper.toBooking(
                dto,
                item,
                userService.getOne(dto.getBookerId())
        );
        if (!item.getAvailable()) {
            rep.save(booking);
            throw new ValidationException("Вещь нет доступна для брони");
        }

        return rep.save(booking);
    }

    @Override
    public Booking approvedBooking(ApprovedBookingDto dto) {
        if (dto.getApproved() == null ||
                dto.getUserId() == null ||
                dto.getBookingId() == null)
            throw new ValidationException("Передано пустое значение");

        Booking booking = getById(dto.getBookingId());
        if (!booking.getItem().getOwner().getId().equals(dto.getUserId()))
            throw new AccessRightsException("Нет прав на правку записи");
        if (dto.getApproved())
            booking.setStatus(StatusBooking.APPROVED);
        else
            booking.setStatus(StatusBooking.REJECTED);
        return rep.save(booking);
    }

    @Override
    public Booking getBooking(GetBookingDto dto) {
        if (dto.getUserId() == null ||
                dto.getBookingId() == null)
            throw new ValidationException("Передано пустое значение");
        userService.contains(dto.getUserId());
        Booking booking = getById(dto.getBookingId());
        if (booking.getItem().getOwner().getId().equals(dto.getUserId()) &&
                booking.getBooker().getId().equals(dto.getUserId()))
            throw new AccessRightsException("Нет прав на получение записи");
        return booking;
    }

    @Override
    public Collection<Booking> getBookingsByState(GetBookingsDto dto) {
        if (dto.getUserId() == null)
            throw new ValidationException("Передано пустое значение");
        if (dto.getState() == null)
            dto.setState(State.ALL);
        userService.contains(dto.getUserId());
        return switch (dto.getState()) {
            case State.ALL -> rep.getBookingsByStateAll(dto.getUserId());
            case State.CURRENT -> rep.getBookingsByStateCurrent(dto.getUserId(), LocalDateTime.now());
            case State.PAST -> rep.getBookingsByStatePast(dto.getUserId(), LocalDateTime.now());
            case State.FUTURE -> rep.getBookingsByStateFuture(dto.getUserId(), LocalDateTime.now());
            case State.WAITING -> rep.getBookingsByStateWaiting(dto.getUserId());
            case State.REJECTED -> rep.getBookingsByStateRejected(dto.getUserId());
            default -> throw new ObjectNotFoundException("Не существует такого состояния бронирования");
        };
    }

    @Override
    public Collection<Booking> getBookingsByOwner(GetBookingsDto dto) {
        if (dto.getUserId() == null)
            throw new ValidationException("Передано пустое значение");
        if (dto.getState() == null)
            dto.setState(State.ALL);
        userService.contains(dto.getUserId());
        return switch (dto.getState()) {
            case State.ALL -> rep.getBookingsOwnerByStateAll(dto.getUserId());
            case State.CURRENT -> rep.getBookingsOwnerByStateCurrent(dto.getUserId(), LocalDateTime.now());
            case State.PAST -> rep.getBookingsOwnerByStatePast(dto.getUserId(), LocalDateTime.now());
            case State.FUTURE -> rep.getBookingsOwnerByStateFuture(dto.getUserId(), LocalDateTime.now());
            case State.WAITING -> rep.getBookingsOwnerByStateWaiting(dto.getUserId());
            case State.REJECTED -> rep.getBookingsOwnerByStateRejected(dto.getUserId());
            default -> throw new ObjectNotFoundException("Не существует такого состояния бронирования");
        };
    }

    private Booking getById(Long id) {
        return rep.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Запись бронирования не найдена"));
    }

    private void contains(Long bookingId) {
        if (rep.existsById(bookingId))
            throw new ObjectNotFoundException("Запись бронирования нее найдена");
    }

}
