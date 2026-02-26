package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemFromRequestDto;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("""
                select r
                from ItemRequest as r
                join User as u on u.id = r.requestor.id
                where r.requestor.id = :requestorId
            """)
    List<ItemRequest> findByRequestorId(Long requestorId);

    @Query("""
            select new ru.practicum.shareit.item.dto.ItemFromRequestDto(
                i.id,
                i.name,
                i.description,
                new ru.practicum.shareit.user.dto.UserDto(i.owner),
                i.available
            )
            from Item as i
            join User as u on u.id = i.owner.id
            where i.request.id = :requestId
            """)
    List<ItemFromRequestDto> getByRequestId(Long requestId);

}
