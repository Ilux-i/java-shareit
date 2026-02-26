package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private CommentRepository commentRepository;

    private final Long userId = 1L;
    private final Long bookingId = 10L;
    private Booking booking;

    @BeforeEach
    void setUp() {
        User booker = new User();
        booker.setId(userId);
        booker.setName("Test User");
        booker.setEmail("test@example.com");

        Item item = new Item();
        item.setId(100L);
        item.setName("Drill");
        item.setAvailable(true);
        item.setOwner(booker);

        booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(StatusBooking.WAITING);
    }

    @Test
    void createBooking_shouldCallService() throws Exception {
        AddBookingDto dto = new AddBookingDto();
        dto.setItemId(100L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        when(bookingService.addBooking(any(AddBookingDto.class)))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.status").value("WAITING"));

        verify(bookingService, times(1)).addBooking(any(AddBookingDto.class));
    }

    @Test
    void approveBooking_shouldCallService() throws Exception {
        when(bookingService.approvedBooking(argThat(dto ->
                dto.getUserId().equals(userId) &&
                        dto.getBookingId().equals(bookingId) &&
                        Boolean.TRUE.equals(dto.getApproved())
        )))
                .thenReturn(booking);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));

        verify(bookingService, times(1)).approvedBooking(argThat(dto ->
                dto.getUserId().equals(userId) &&
                        dto.getBookingId().equals(bookingId) &&
                        Boolean.TRUE.equals(dto.getApproved())
        ));
    }

    @Test
    void getBookingById_shouldCallService() throws Exception {
        when(bookingService.getBooking(argThat(dto ->
                dto.getUserId().equals(userId) &&
                        dto.getBookingId().equals(bookingId))))
                .thenReturn(booking);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));

        verify(bookingService, times(1)).getBooking(any(GetBookingDto.class));
    }

    @Test
    void getBookings_shouldCallServiceWithParams() throws Exception {
        when(bookingService.getBookingsByState(argThat(dto ->
                dto.getUserId().equals(userId) &&
                        dto.getState() == State.ALL)))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingId));

        verify(bookingService, times(1)).getBookingsByState(any(GetBookingsDto.class));
    }

    @Test
    void getBookings_shouldHandleInvalidState() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "INVALID_STATE"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).getBookingsByState(any());
    }
}