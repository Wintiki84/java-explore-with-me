package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Details;
import ru.practicum.validator.Private;

import java.util.List;

@Getter
@Builder
public class ListEventDtoResponse {
    @JsonView({Details.class, AdminDetails.class, Private.class})
    @JsonValue
    private List<EventDtoResponse> events;
}
