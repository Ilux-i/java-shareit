package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Table(name = "bookings")
@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // уникальный идентификатор бронирования;

    @Column(name = "start_date")
    private LocalDateTime start;        // дата и время начала бронирования;

    @Column(name = "end_date")
    private LocalDateTime end;          // дата и время конца бронирования;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;              // вещь, которую пользователь бронирует;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User booker;            // пользователь, который осуществляет бронирование;

    @Column(name = "status")
    private StatusBooking status;   // статус бронирования

}
