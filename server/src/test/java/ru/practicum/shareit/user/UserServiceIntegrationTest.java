package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private CreateUserDto createDto;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        createDto = new CreateUserDto("Ivan Ivanov", "ivan@example.com");
    }

    @Test
    void add_shouldSaveUserToH2() {
        User savedUser = userService.add(createDto);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Ivan Ivanov");
        assertThat(savedUser.getEmail()).isEqualTo("ivan@example.com");

        // Проверка, что запись реально есть в БД
        assertThat(userRepository.existsById(savedUser.getId())).isTrue();
    }

    @Test
    void add_shouldThrowValidationExceptionOnInvalidEmail() {
        createDto = new CreateUserDto(createDto.getName(), "invalid-email-without-at");

        assertThatThrownBy(() -> userService.add(createDto)).isInstanceOf(ValidationException.class).hasMessageContaining("Ошибка валидации email");
    }

    @Test
    void add_shouldThrowDuplicateEmailException() {
        userService.add(createDto);

        CreateUserDto duplicateDto = new CreateUserDto("Petr Petrov", "ivan@example.com");

        assertThatThrownBy(() -> userService.add(duplicateDto)).isInstanceOf(DuplicateEmailException.class).hasMessageContaining("Данный email уже существует");
    }

    @Test
    void update_shouldModifyUserInH2() {
        User created = userService.add(createDto);

        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setId(created.getId());
        updateDto.setName("Ivan Updated");
        updateDto.setEmail("ivan.new@example.com");

        User updated = userService.update(updateDto);

        assertThat(updated.getName()).isEqualTo("Ivan Updated");
        assertThat(updated.getEmail()).isEqualTo("ivan.new@example.com");

        User fromDb = userRepository.findById(created.getId()).orElseThrow();
        assertThat(fromDb.getName()).isEqualTo("Ivan Updated");
    }

    @Test
    void update_shouldPreserveFieldsIfNull() {
        User created = userService.add(createDto);

        UpdateUserDto partialDto = new UpdateUserDto();
        partialDto.setId(created.getId());
        partialDto.setName(null);
        partialDto.setEmail(null);

        User updated = userService.update(partialDto);

        assertThat(updated.getName()).isEqualTo("Ivan Ivanov");
        assertThat(updated.getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    void getOne_shouldReturnUserFromH2() {
        User created = userService.add(createDto);

        User found = userService.getOne(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    void getOne_shouldThrowIfNotFound() {
        Long fakeId = 999L;

        assertThatThrownBy(() -> userService.getOne(fakeId)).isInstanceOf(ObjectNotFoundException.class).hasMessageContaining("Пользователь не найден");
    }

    @Test
    void delete_shouldRemoveUserFromH2() {
        User created = userService.add(createDto);

        userService.delete(created.getId());

        assertThat(userRepository.existsById(created.getId())).isFalse();
    }

    @Test
    void delete_shouldThrowIfUserNotFound() {
        Long fakeId = 999L;

        assertThatThrownBy(() -> userService.delete(fakeId)).isInstanceOf(ObjectNotFoundException.class);
    }
}