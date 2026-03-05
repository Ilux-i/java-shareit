package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(long ownerId);

    @Query("""
            select item
            from Item as item
            join item.owner as o
            where item.available = true
                    and (lower(item.name) like lower(concat('%', :text, '%'))
                    or lower(item.description) like lower(concat('%', :text, '%')))
            """)
    Collection<Item> searchByText(String text);

}