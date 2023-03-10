package ru.practicum.server.category.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Details;
import ru.practicum.validator.Private;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@Jacksonized
public class CategoryDto {
    @JsonView({AdminDetails.class, Private.class, Details.class})
    private Long id;
    @JsonView({AdminDetails.class, Private.class, Details.class})
    @Pattern(regexp = "^[^ ].*[^ ]$", message = "Ошибка в названии категории")
    @Size(max = 255)
    @NotNull(message = "Название не должно быть пустым")
    private String name;
}
