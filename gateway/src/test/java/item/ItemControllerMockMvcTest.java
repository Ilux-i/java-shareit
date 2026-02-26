package item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ShareItGateway;
import ru.practicum.item.ItemClient;
import ru.practicum.item.dto.ItemDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
class ItemControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    private ItemDto itemDto;
    private final Long userId = 1L;
    private final Long itemId = 10L;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setOwnerId(userId);
        itemDto.setAvailable(true);
    }

    @Test
    void add_shouldCallClientWithCorrectArgs() throws Exception {
        // Мокаем ответ от клиента
        when(itemClient.add(eq(userId), any(ItemDto.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(itemDto));

        String json = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("Drill"));

        verify(itemClient, times(1)).add(eq(userId), any(ItemDto.class));
    }

    @Test
    void add_shouldReturnBadRequestIfHeaderMissing() throws Exception {
        String json = objectMapper.writeValueAsString(itemDto);

        // Запрос без обязательного заголовка X-Sharer-User-Id
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); // Ожидается 400 из-за @PositiveOrZero или отсутствия заголовка

        verify(itemClient, never()).add(anyLong(), any());
    }

    @Test
    void update_shouldCallClientWithCorrectArgs() throws Exception {
        when(itemClient.update(eq(userId), eq(itemId), any(ItemDto.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(itemDto));

        itemDto.setName("Updated Drill");
        String json = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Drill"));

        verify(itemClient, times(1)).update(eq(userId), eq(itemId), any(ItemDto.class));
    }

    @Test
    void getOne_shouldCallClient() throws Exception {
        when(itemClient.getOne(itemId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(itemDto));

        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));

        verify(itemClient, times(1)).getOne(itemId);
    }

    @Test
    void getByOwner_shouldCallClient() throws Exception {
        when(itemClient.getByOwner(userId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(java.util.List.of(itemDto)));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemId));

        verify(itemClient, times(1)).getByOwner(userId);
    }

    @Test
    void search_shouldCallClientWithTextParam() throws Exception {
        String searchText = "drill";
        when(itemClient.search(eq(userId), eq(searchText)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(java.util.List.of(itemDto)));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", searchText))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drill"));

        verify(itemClient, times(1)).search(eq(userId), eq(searchText));
    }

    @Test
    void search_shouldReturnBadRequestIfTextMissing() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId))
                // Ожидается 400, так как параметр text помечен как @NotNull в контроллере (или должен быть)
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).search(anyLong(), anyString());
    }
}
