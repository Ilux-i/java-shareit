package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User testUser;
    private CreateUserDto createDto;
    private UpdateUserDto updateDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");

        createDto = new CreateUserDto(
                "John Doe",
                "john@example.com"
        );

        updateDto = new UpdateUserDto(
                1L,
                "Jane Doe",
                "jane@example.com"
        );
    }

    @Test
    void add_shouldReturnCreatedUser() throws Exception {
        when(userService.add(any(CreateUserDto.class))).thenReturn(testUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).add(any(CreateUserDto.class));
    }

    @Test
    void add_shouldHandleValidationException() throws Exception {
        doThrow(new ValidationException("Ошибка валидации email"))
                .when(userService).add(any(CreateUserDto.class));
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void add_shouldHandleDuplicateEmailException() throws Exception {
        doThrow(new DuplicateEmailException("Данный email уже существует"))
                .when(userService).add(any(CreateUserDto.class));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void update_shouldReturnUpdatedUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane@example.com");

        when(userService.update(any(UpdateUserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe"));

        verify(userService, times(1)).update(any(UpdateUserDto.class));
    }

    @Test
    void update_shouldHandleObjectNotFoundException() throws Exception {
        doThrow(new ObjectNotFoundException("Пользователь не найден"))
                .when(userService).update(any(UpdateUserDto.class));

        mockMvc.perform(patch("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOne_shouldReturnUser() throws Exception {
        when(userService.getOne(1L)).thenReturn(testUser);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).getOne(1L);
    }

    @Test
    void getOne_shouldHandleObjectNotFoundException() throws Exception {
        doThrow(new ObjectNotFoundException("Пользователь не найден"))
                .when(userService).getOne(999L);

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldCallServiceAndReturnOk() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    void delete_shouldHandleObjectNotFoundException() throws Exception {
        doThrow(new ObjectNotFoundException("Пользователь не найден"))
                .when(userService).delete(999L);

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound());
    }
}