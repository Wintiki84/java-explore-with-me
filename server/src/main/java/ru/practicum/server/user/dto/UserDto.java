package ru.practicum.server.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    private Long id;
    @Pattern(regexp = "^[a-z]([a-zA-Z0-9]*)?$", message = "некорректное имя")
    @Size(max = 255)
    @NotNull(message = "имя не должно быть null")
    private String name;
    @Email(message = "некорректный email")
    @NotNull
    private String email;
}
