package ru.practicum.shareit.comment;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // уникальный идентификатор комментария;

    @Column(name = "text")
    private String text;            // содержимое комментария;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;              // вещь, к которой относится комментарий;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;            // автор комментария;

    @Column(name = "created_date")
    private LocalDateTime created;  // дата создания комментария
}
