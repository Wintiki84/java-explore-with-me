package ru.practicum.product.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Create;
import ru.practicum.validator.Private;

import javax.validation.constraints.*;

public class ProductDtoRequest {
    @JsonView({AdminDetails.class, Private.class})
    @Null(groups = {Create.class}, message = "Должно быть пустым")
    @Positive(message = "Должно быть больше нуля")
    private Long id;
    @JsonView({AdminDetails.class, Private.class})
    @Pattern(groups = {Create.class}, regexp = "^[^ ].*[^ ]$", message = "некорректное имя")
    @Size(groups = {Create.class}, max = 255)
    @NotNull(groups = {Create.class}, message = "Не должно быть null")
    @NotBlank(groups = {Create.class}, message = "Не должно быть пустым")
    private String name;
    @JsonView({AdminDetails.class, Private.class})
    @NotNull(groups = {Create.class}, message = "Не должно быть null")
    @NotBlank(groups = {Create.class}, message = "Не должно быть пустым")
    private String description;
    @JsonView({AdminDetails.class, Private.class})
    @NotNull(groups = {Create.class}, message = "Не должно быть null")
    private Long price;
}
