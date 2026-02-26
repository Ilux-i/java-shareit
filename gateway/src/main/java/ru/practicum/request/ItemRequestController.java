package ru.practicum.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.CreateRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final RequestClient client;

    @PostMapping()
    public ResponseEntity<Object> add(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody @Valid CreateRequestDto dto
    ) {
        return client.add(userId, dto);
    }

    @GetMapping()
    public ResponseEntity<Object> getByUserId(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        return client.getByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(
    ) {
        return client.getAll();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @PositiveOrZero @RequestHeader(USER_ID_HEADER) Long userId,
            @PositiveOrZero @PathVariable("requestId") Long requestId
    ) {
        return client.getById(userId, requestId);
    }
    // у теста постмена не вставляется requestId в URLку

}
