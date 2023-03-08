package ru.practicum.server.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.validator.Private;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class RequestListDto {
    @JsonValue
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    private List<RequestDto> requests;
}
