package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ShareItGateway;
import ru.practicum.user.UserClient;
import ru.practicum.user.dto.UpdateUserDto;
import ru.practicum.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Test
    void add_shouldReturnOk_whenValidUser() throws Exception {
        UserDto dto = new UserDto(null, "Ivan", "ivan@test.ru");
        when(userClient.add(any(UserDto.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(dto));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@test.ru"));

        verify(userClient, times(1)).add(any(UserDto.class));
    }

    @Test
    void add_shouldReturnBadRequest_whenNameIsEmpty() throws Exception {
        UserDto dto = new UserDto(null, "", "ivan@test.ru"); // Пустое имя

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).add(any());
    }

    @Test
    void add_shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {
        UserDto dto = new UserDto(null, "Ivan", "invalid-email"); // Неверный email

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).add(any());
    }

    @Test
    void update_shouldReturnOk_whenValidData() throws Exception {
        Long userId = 1L;
        UpdateUserDto dto = new UpdateUserDto(null, "New Name", "new@test.ru");

        when(userClient.update(eq(userId), any(UpdateUserDto.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(dto));

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userClient, times(1)).update(eq(userId), any(UpdateUserDto.class));
    }

    @Test
    void update_shouldReturnBadRequest_whenUserIdIsNegative() throws Exception {
        Long userId = -1L;
        UpdateUserDto dto = new UpdateUserDto(null, "Name", "test@test.ru");

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // @PositiveOrZero отработает

        verify(userClient, never()).update(any(), any());
    }

    @Test
    void getUser_shouldReturnOk_whenUserExists() throws Exception {
        Long userId = 5L;
        UserDto responseDto = new UserDto(userId, "User Five", "five@test.ru");

        when(userClient.getUser(userId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(responseDto));

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("User Five"));

        verify(userClient, times(1)).getUser(userId);
    }

    @Test
    void getUser_shouldReturnBadRequest_whenUserIdIsNegative() throws Exception {
        Long userId = -10L;

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).getUser(any());
    }

    @Test
    void delete_shouldReturnNoContent_whenSuccess() throws Exception {
        Long userId = 3L;
        doNothing().when(userClient).delete(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userClient, times(1)).delete(userId);
    }
}
