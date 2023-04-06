package ru.practicum.client.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Create;
import ru.practicum.validator.Private;

import javax.validation.constraints.*;

@Data
@Builder
public class ClientDto {
    @JsonView({AdminDetails.class, Private.class})
    @Null(groups = {Create.class}, message = "Должно быть пустым")
    private Long id;
    @JsonView({AdminDetails.class, Private.class})
    @Pattern(groups = {Create.class}, regexp = "^[^ ].*[^ ]$", message = "некорректное имя")
    @Size(groups = {Create.class}, max = 255)
    @NotNull(groups = {Create.class}, message = "Не должно быть null")
    @NotBlank(groups = {Create.class}, message = "Не должно быть пустым")
    private String name;
    @JsonView({AdminDetails.class, Private.class})
    private Integer personalDiscount1;
    @JsonView({AdminDetails.class, Private.class})
    private Integer personalDiscount2;
}
