package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.validator.Details;
import ru.practicum.validator.Private;

import java.util.List;

@Getter
@Builder
public class ListEventShortDto {
    @JsonValue
    @JsonView({Details.class, Private.class})
    private List<EventShortDto> events;
}
