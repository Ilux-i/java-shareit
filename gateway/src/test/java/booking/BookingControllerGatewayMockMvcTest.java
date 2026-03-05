package booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ShareItGateway;
import ru.practicum.booking.BookingClient;
import ru.practicum.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
class BookingControllerGatewayMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private final Long userId = 1L;
    private final Long bookingId = 10L;

    @Test
    void createBooking_shouldCallClient() throws Exception {
        BookItemRequestDto dto = new BookItemRequestDto();
        dto.setItemId(100L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        when(bookingClient.bookItem(eq(userId), any(BookItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).bookItem(eq(userId), any(BookItemRequestDto.class));
    }

    @Test
    void approveBooking_shouldCallClient() throws Exception {
        when(bookingClient.approvedBooking(eq(userId), eq(bookingId), eq(true)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).approvedBooking(userId, bookingId, true);
    }

    @Test
    void getBookings_shouldCallClient() throws Exception {
        when(bookingClient.getBookings(eq(userId), any(), eq(0), eq(10)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getBookings(eq(userId), any(), eq(0), eq(10));
    }
}