package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
}
