package ru.practicum.shareit.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Table(name = "users")
@Entity
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // уникальный идентификатор пользователя;

    @Column(name = "name", nullable = false)
    private String name;    // имя или логин пользователя;

    @Column(name = "email", nullable = false, unique = true)
    private String email;    // адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).

}
