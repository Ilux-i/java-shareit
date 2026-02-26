package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.item.dto.TextDto;
import ru.practicum.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(Long userId, ItemDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, TextDto dto) {
        return post("/" + itemId + "/comment", userId, dto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto dto) {
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> getOne(Long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getByOwner(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> search(Long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

}
