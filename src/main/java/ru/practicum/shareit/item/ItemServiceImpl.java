package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.NewCommentDto;
import ru.practicum.shareit.comment.ResponseCommentDto;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository rep;
    @Autowired
    private final UserService userService;
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final BookingRepository bookingRepository;

    @Override
    public Item add(NewItemDto dto) {
        User owner = userService.getOne(dto.getOwnerId());
        Item item = ItemMapper.toItem(dto, owner);
        validItem(item);
        rep.save(item);
        return item;
    }

    @Override
    public ResponseCommentDto addComment(NewCommentDto dto) {
        if (dto.getUserId() == null ||
                dto.getItemId() == null ||
                dto.getText() == null ||
                dto.getText().isEmpty())
            throw new ValidationException("Отсутствует параметр id");
        User author = userService.getOne(dto.getUserId());
        Item item = rep.findById(dto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Item с id: " + dto.getItemId() + " не найден")
                );
        ;

        if (!commentRepository.checkingAbilityComment(
                dto.getUserId(),
                dto.getItemId(),
                LocalDateTime.now()
        )
        ) throw new ValidationException("Нет возможности публикации отзыва");
        Comment comment = commentRepository.save(
                Comment.builder()
                        .item(item)
                        .author(author)
                        .text(dto.getText())
                        .created(LocalDateTime.now())
                        .build()
        );
        return ResponseCommentDto.builder()
                .id(comment.getId())
                .item(comment.getItem())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    @Override
    @Transactional
    public Item update(UpdateItemDto dto) {
        if (dto.getId() == null)
            throw new ValidationException("Отсутствует параметр id");
        Item oldItem = rep.getOne(dto.getId());
        if (!oldItem.getOwner().getId().equals(dto.getOwner()))
            throw new AccessRightsException("Нет доступа");

        return rep.save(updated(oldItem, dto));
    }

    @Override
    public Item getOne(Long itemId) {
        return rep.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Item с id: " + itemId + " не найден")
                );
    }

    @Override
    public ResponseItemDto getFullById(Long itemId) {
        ResponseItemDto response = new ResponseItemDto(getOne(itemId));
        LocalDateTime now = LocalDateTime.now();
        response.setLastBooking(bookingRepository.findLastBookingById(itemId, now).orElse(null));
        response.setLastBooking(null);
        response.setNextBooking(bookingRepository.findNextBookingById(itemId, now).orElse(null));
        response.setComments(commentRepository.findCommentsByItemId(itemId));
        return response;
    }

    @Override
    public Collection<Item> getByOwner(Long id) {
        validOwner(id);
        return rep.findByOwnerId(id);
    }

    @Override
    public Collection<Item> search(SearchItemDto dto) {
        if (dto.getText() == null || dto.getText().isEmpty()) {
            return new ArrayList<>();
        }
        return rep.searchByText(dto.getText());
    }

    private void validItem(Item item) {
        if (item.getName() == null ||
                item.getName().isEmpty() ||
                item.getDescription() == null ||
                item.getOwner() == null ||
                item.getAvailable() == null
        ) throw new ValidationException("Ошибка валидации item с id: " + item.getId());
        userService.contains(item.getOwner().getId());
    }

    private void validOwner(Long ownerId) {
        if (ownerId == null)
            throw new ValidationException("Ошибка валидации ownerId");
        userService.contains(ownerId);
    }

    private void contains(Long itemId) {
        if (!rep.existsById(itemId))
            throw new ObjectNotFoundException("Item с id: " + itemId + " не найден");
    }

    private Item updated(Item oldItem, UpdateItemDto dto) {
        Item newItem = new Item();
        // Обновление данных
        if (dto.getName() == null || dto.getName().isEmpty())
            newItem.setName(oldItem.getName());
        else
            newItem.setName(dto.getName());

        if (dto.getDescription() == null || dto.getDescription().isEmpty())
            newItem.setDescription(oldItem.getDescription());
        else
            newItem.setDescription(dto.getDescription());

        newItem.setOwner(oldItem.getOwner());

        if (dto.getAvailable() == null)
            newItem.setAvailable(oldItem.getAvailable());
        else
            newItem.setAvailable(dto.getAvailable());

        if (dto.getRequest() == null || dto.getRequest().isEmpty())
            newItem.setRequest(oldItem.getRequest());
        else
            newItem.setRequest(dto.getRequest());

        return newItem;
    }

}
