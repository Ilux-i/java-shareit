package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Table(name = "request")
@Entity()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // уникальный идентификатор запроса;

    @Column(name = "description")
    private String description;       // текст запроса, содержащий описание требуемой вещи;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requestor;           // пользователь, создавший запрос;

    @Column(name = "created")
    private LocalDateTime created;    // дата и время создания запроса

    ItemRequest(User requestor, CreateRequestDto dto) {
        this.description = dto.getDescription();
        this.requestor = requestor;
        this.created = LocalDateTime.now();
    }

}
