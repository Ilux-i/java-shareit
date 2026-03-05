package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserService userService;

    private User owner;
    private User otherUser;
    private Item item;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();

        owner = createUser("Owner", "owner@test.com");
        otherUser = createUser("Other", "other@test.com");

        NewItemDto newItemDto = new NewItemDto(owner.getId(), createItemDto("Drill", "Powerful", true));
        item = itemService.add(newItemDto);
    }

    private User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userService.add(new ru.practicum.shareit.user.dto.CreateUserDto(user));
    }

    private ru.practicum.shareit.item.dto.ItemDto createItemDto(String name, String desc, Boolean available) {
        var dto = new ru.practicum.shareit.item.dto.ItemDto();
        dto.setName(name);
        dto.setDescription(desc);
        dto.setAvailable(available);
        return dto;
    }

    @Test
    void add_shouldSaveItemAndReturnIt() {
        NewItemDto dto = new NewItemDto(owner.getId(), createItemDto("Hammer", "Heavy", true));

        Item savedItem = itemService.add(dto);

        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo("Hammer");
        assertThat(savedItem.getOwner().getId()).isEqualTo(owner.getId());
        assertThat(itemRepository.existsById(savedItem.getId())).isTrue();
    }

    @Test
    void add_shouldThrowIfOwnerNotFound() {
        NewItemDto dto = new NewItemDto(999L, createItemDto("Saw", "Sharp", true));

        assertThatThrownBy(() -> itemService.add(dto))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Пользователь не найден");
    }

    @Test
    void update_shouldChangeItemFields() {
        UpdateItemDto dto = new UpdateItemDto(item.getId(), owner.getId(), createItemDto("Updated Drill", "New Desc", false));

        Item updated = itemService.update(dto);

        assertThat(updated.getName()).isEqualTo("Updated Drill");
        assertThat(updated.getDescription()).isEqualTo("New Desc");
        assertThat(updated.getAvailable()).isFalse();
    }

    @Test
    void update_shouldThrowAccessRightsExceptionIfNotOwner() {
        UpdateItemDto dto = new UpdateItemDto(item.getId(), otherUser.getId(), createItemDto("Hacked", "Hack", true));

        assertThatThrownBy(() -> itemService.update(dto))
                .isInstanceOf(AccessRightsException.class)
                .hasMessageContaining("Нет доступа");
    }

    @Test
    void getOne_shouldReturnItem() {
        Item found = itemService.getOne(item.getId());

        assertThat(found.getId()).isEqualTo(item.getId());
        assertThat(found.getName()).isEqualTo("Drill");
    }

    @Test
    void getOne_shouldThrowIfNotFound() {
        assertThatThrownBy(() -> itemService.getOne(999L))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void getByOwner_shouldReturnOnlyOwnerItems() {
        NewItemDto dto2 = new NewItemDto(otherUser.getId(), createItemDto("Saw", "Sharp", true));
        itemService.add(dto2);

        Collection<Item> ownerItems = itemService.getByOwner(owner.getId());

        assertThat(ownerItems).hasSize(1);
        assertThat(ownerItems.iterator().next().getName()).isEqualTo("Drill");
    }

    @Test
    void search_shouldReturnItemsMatchingText() {
        NewItemDto dto2 = new NewItemDto(owner.getId(), createItemDto("Saw", "Electric saw", true));
        itemService.add(dto2);

        Collection<Item> result = itemService.search(new ru.practicum.shareit.item.dto.SearchItemDto(owner.getId(), "drill"));

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getName()).isEqualTo("Drill");
    }

    @Test
    void search_shouldReturnEmptyIfTextIsEmpty() {
        Collection<Item> result = itemService.search(new ru.practicum.shareit.item.dto.SearchItemDto(owner.getId(), ""));

        assertThat(result).isEmpty();
    }
}
