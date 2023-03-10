package ru.practicum.server.request.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.request.enums.RequestStatus;
import ru.practicum.validator.Private;

import javax.validation.constraints.*;

@Data
@Builder
public class RequestDto {
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    @Positive(message = "Должно быть больше нуля")
    private Long id;
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    @Positive(message = "Должно быть больше нуля")
    private Long event;
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    @Positive(message = "Должно быть больше нуля")
    private Long requester;
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    private RequestStatus status;
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    @NotBlank(message = "Не должно быть пустым")
    private String created;
}
