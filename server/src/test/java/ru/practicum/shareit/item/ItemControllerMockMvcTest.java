package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.NewCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.dto.TextDto;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private final Long userId = 1L;
    private final Long itemId = 10L;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setId(userId);
        owner.setName("Owner");
        owner.setEmail("owner@test.com");

        item = new Item();
        item.setId(itemId);
        item.setName("Drill");
        item.setDescription("Powerful");
        item.setAvailable(true);
        item.setOwner(owner);

        itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful");
        itemDto.setAvailable(true);
    }

    @Test
    void add_shouldCallServiceAndReturnItem() throws Exception {
        NewItemDto expectedDto = any(NewItemDto.class); // Упрощенная проверка
        when(itemService.add(expectedDto)).thenReturn(item);

        String json = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("Drill"));

        verify(itemService, times(1)).add(any(NewItemDto.class));
    }

    @Test
    void update_shouldCallServiceAndReturnItem() throws Exception {
        when(itemService.update(any(UpdateItemDto.class))).thenReturn(item);

        String json = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Drill"));

        verify(itemService, times(1)).update(any(UpdateItemDto.class));
    }

    @Test
    void update_shouldHandleAccessRightsException() throws Exception {
        doThrow(new AccessRightsException("Нет доступа"))
                .when(itemService).update(any(UpdateItemDto.class));

        String json = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void getOne_shouldCallServiceAndReturnResponseItemDto() throws Exception {
        ResponseItemDto responseDto = new ResponseItemDto(item);
        when(itemService.getFullById(itemId)).thenReturn(responseDto);

        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));

        verify(itemService, times(1)).getFullById(itemId);
    }

    @Test
    void getByOwner_shouldCallService() throws Exception {
        when(itemService.getByOwner(userId)).thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drill"));

        verify(itemService, times(1)).getByOwner(userId);
    }

    @Test
    void search_shouldCallServiceWithText() throws Exception {
        String text = "drill";
        when(itemService.search(any(ru.practicum.shareit.item.dto.SearchItemDto.class)))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drill"));

        verify(itemService, times(1)).search(any(ru.practicum.shareit.item.dto.SearchItemDto.class));
    }

    @Test
    void addComment_shouldCallService() throws Exception {
        TextDto textDto = new TextDto("Great item!");
        ResponseCommentDto responseDto = ResponseCommentDto.builder()
                .id(1L)
                .text("Great item!")
                .build();
        when(itemService.addComment(any(NewCommentDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(textDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Great item!"));

        verify(itemService).addComment(any(NewCommentDto.class));
    }
}
