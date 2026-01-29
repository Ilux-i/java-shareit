package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.SearchItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemStorage itemStorage;

    @Override
    public Item add(NewItemDto newItemDto) {
        return itemStorage.add(newItemDto);
    }

    @Override
    public Item update(Long itemId, NewItemDto newItemDto) {
        return itemStorage.update(
                new UpdateItemDto(newItemDto, itemStorage.getOne(itemId))
        );
    }

    @Override
    public Item getOne(GetItemDto getItemDto) {
        return itemStorage.getOne(getItemDto.getItemId());
    }

    @Override
    public Collection<Item> getByOwner(Long id) {
        return itemStorage.getByOwner(id);
    }

    @Override
    public Collection<Item> search(SearchItemDto searchItemDto) {
        return itemStorage.search(searchItemDto.getText());
    }

}
