package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private Long id;                // уникальный идентификатор вещи;
    private String name;            // краткое название;
    private String description;     // развёрнутое описание;
    private Long ownerId;             // владелец вещи;
    private Boolean available;   // статус о том, доступна или нет вещь для аренды;
    private String request;         // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

}
