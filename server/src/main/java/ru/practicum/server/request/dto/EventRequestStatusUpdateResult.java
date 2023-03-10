package ru.practicum.server.request.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.validator.Private;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResult {
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    private List<RequestDto> confirmedRequests;
    @JsonView({Private.class})
    @NotNull(message = "Не должно быть null")
    private List<RequestDto> rejectedRequests;
}
