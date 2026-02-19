package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.NewCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.SearchItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    // Добавление Item
    public Item add(NewItemDto newItemDto);

    // Добавление комментария
    ResponseCommentDto addComment(NewCommentDto dto);

    // Редактирование Item
    Item update(UpdateItemDto dto);

    // Получение Item по его id
    Item getOne(Long itemId);

    ResponseItemDto getFullById(Long itemId);

    // Получение Items по владельцу
    Collection<Item> getByOwner(Long id);

    Collection<Item> search(SearchItemDto searchItemDto);

}
