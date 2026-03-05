package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestsAlongWithAnswersDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository rep;
    private final UserService userService;

    public ItemRequest getOne(Long requestId) {
        if (requestId != null)
            return rep.findById(requestId).orElseThrow(
                    () -> new ObjectNotFoundException("Запрос с id: " + requestId + " не найден")
            );
        return null;
    }

    public ItemRequest add(Long userId, CreateRequestDto dto) {
        return rep.save(new ItemRequest(userService.getOne(userId), dto));
    }

    public List<ItemRequest> getByUserId(Long requestId) {
        return rep.findByRequestorId(requestId);
    }

    public RequestsAlongWithAnswersDto getById(Long requestId) {
        if (requestId != null) {
            ItemRequest req = rep.findById(requestId).orElseThrow(
                    () -> new ObjectNotFoundException("Запрос с id: " + requestId + " не найден")
            );
            return new RequestsAlongWithAnswersDto(
                    req,
                    rep.getByRequestId(requestId)
            );
        } else
            return null;
    }

    public List<ItemRequestDto> getAll() {
        return null;
    }

}
