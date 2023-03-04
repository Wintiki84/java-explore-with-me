package ru.practicum.server.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@Jacksonized
public class CategoryDto {
    private Long id;
    @Pattern(regexp = "^[a-z]([a-zA-Z0-9]*)?$", message = "Ошибка в названии категории")
    @Size(max = 255)
    @NotNull(message = "Название не должно быть пустым")
    private String name;
}
