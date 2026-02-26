package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.ApprovedBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    // ИСПОЛЬЗУЕМ РЕПОЗИТОРИИ ДЛЯ ПОДГОТОВКИ ДАННЫХ В БД
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User booker;
    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@test.com");
        booker = userRepository.save(booker);

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        owner = userRepository.save(owner);

        item = new Item();
        item.setName("Drill");
        item.setDescription("Powerful drill");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);
    }

    @Test
    void addBooking_shouldSaveBookingWithWaitingStatus() {
        AddBookingDto dto = new AddBookingDto();
        dto.setItemId(item.getId());
        dto.setBookerId(booker.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Booking created = bookingService.addBooking(dto);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getBooker().getId()).isEqualTo(booker.getId());
        assertThat(created.getItem().getId()).isEqualTo(item.getId());
        assertThat(created.getStatus()).isEqualTo(StatusBooking.WAITING);
    }

    @Test
    void addBooking_shouldThrowIfDatesInvalid() {
        AddBookingDto dto = new AddBookingDto();
        dto.setItemId(item.getId());
        dto.setBookerId(booker.getId());
        dto.setStart(LocalDateTime.now().plusDays(2));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> bookingService.addBooking(dto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("невозможный временной отрезок");
    }

    @Test
    void addBooking_shouldThrowIfItemNotAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);

        AddBookingDto dto = new AddBookingDto();
        dto.setItemId(item.getId());
        dto.setBookerId(booker.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        assertThatThrownBy(() -> bookingService.addBooking(dto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("нет доступна");
    }

    @Test
    void approvedBooking_shouldChangeStatusToApproved() {
        AddBookingDto createDto = new AddBookingDto();
        createDto.setItemId(item.getId());
        createDto.setBookerId(booker.getId());
        createDto.setStart(LocalDateTime.now().plusDays(1));
        createDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking booking = bookingService.addBooking(createDto);

        ApprovedBookingDto approveDto = new ApprovedBookingDto();
        approveDto.setUserId(owner.getId());
        approveDto.setBookingId(booking.getId());
        approveDto.setApproved(true);

        Booking updated = bookingService.approvedBooking(approveDto);

        assertThat(updated.getStatus()).isEqualTo(StatusBooking.APPROVED);
    }

    @Test
    void approvedBooking_shouldChangeStatusToRejected() {
        AddBookingDto createDto = new AddBookingDto();
        createDto.setItemId(item.getId());
        createDto.setBookerId(booker.getId());
        createDto.setStart(LocalDateTime.now().plusDays(1));
        createDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking booking = bookingService.addBooking(createDto);

        ApprovedBookingDto approveDto = new ApprovedBookingDto();
        approveDto.setUserId(owner.getId());
        approveDto.setBookingId(booking.getId());
        approveDto.setApproved(false);

        Booking updated = bookingService.approvedBooking(approveDto);

        assertThat(updated.getStatus()).isEqualTo(StatusBooking.REJECTED);
    }

    @Test
    void approvedBooking_shouldThrowIfNotOwner() {
        AddBookingDto createDto = new AddBookingDto();
        createDto.setItemId(item.getId());
        createDto.setBookerId(booker.getId());
        createDto.setStart(LocalDateTime.now().plusDays(1));
        createDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking booking = bookingService.addBooking(createDto);

        ApprovedBookingDto approveDto = new ApprovedBookingDto();
        approveDto.setUserId(booker.getId());
        approveDto.setBookingId(booking.getId());
        approveDto.setApproved(true);

        assertThatThrownBy(() -> bookingService.approvedBooking(approveDto))
                .isInstanceOf(AccessRightsException.class)
                .hasMessageContaining("Нет прав");
    }

    @Test
    void getBooking_shouldReturnBooking() {
        AddBookingDto createDto = new AddBookingDto();
        createDto.setItemId(item.getId());
        createDto.setBookerId(booker.getId());
        createDto.setStart(LocalDateTime.now().plusDays(1));
        createDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking created = bookingService.addBooking(createDto);

        GetBookingDto getDto = new GetBookingDto();
        getDto.setUserId(booker.getId());
        getDto.setBookingId(created.getId());

        Booking found = bookingService.getBooking(getDto);

        assertThat(found.getId()).isEqualTo(created.getId());
    }

    @Test
    void getBookingsByState_shouldReturnListForAll() {
        for (int i = 0; i < 3; i++) {
            AddBookingDto dto = new AddBookingDto();
            dto.setItemId(item.getId());
            dto.setBookerId(booker.getId());
            dto.setStart(LocalDateTime.now().plusDays(i + 1));
            dto.setEnd(LocalDateTime.now().plusDays(i + 2));
            bookingService.addBooking(dto);
        }

        GetBookingsDto dto = new GetBookingsDto();
        dto.setUserId(booker.getId());
        dto.setState(State.ALL);

        Collection<Booking> bookings = bookingService.getBookingsByState(dto);

        assertThat(bookings).hasSize(3);
    }

    @Test
    void getBookingsByOwner_shouldReturnListForAll() {
        AddBookingDto dto = new AddBookingDto();
        dto.setItemId(item.getId());
        dto.setBookerId(booker.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.addBooking(dto);

        GetBookingsDto getDto = new GetBookingsDto();
        getDto.setUserId(owner.getId());
        getDto.setState(State.ALL);

        Collection<Booking> bookings = bookingService.getBookingsByOwner(getDto);

        assertThat(bookings).hasSize(1);
    }
}