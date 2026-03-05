package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private Long id;
    private String name;
    @Email
    private String email;
}
