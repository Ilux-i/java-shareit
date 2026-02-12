package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;

@Table(name = "items")
@Entity
@Getter
@Setter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // уникальный идентификатор вещи;

    @Column(name = "name")
    private String name;            // краткое название;

    @Column(name = "description")
    private String description;     // развёрнутое описание;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;             // владелец вещи;

    @Column(name = "available")
    private Boolean available;   // статус о том, доступна или нет вещь для аренды;

    @Column(name = "request")
    private String request;         // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

}
