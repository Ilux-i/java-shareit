package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestsAlongWithAnswersDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final RequestService service;

    @PostMapping()
    public ItemRequest add(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody CreateRequestDto dto
    ) {
        return service.add(userId, dto);
    }

    @GetMapping()
    public List<ItemRequest> getByUserId(
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        return service.getByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public RequestsAlongWithAnswersDto getById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable("requestId") Long requestId
    ) {
        return service.getById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(
    ) {
        return service.getAll();
    }

}
