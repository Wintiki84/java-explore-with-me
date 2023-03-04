package ru.practicum.server.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.event.location.Location;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "длин аннотации длжна быть min = 20, max = 2000n")
    private String annotation;
    @Min(1)
    private Long category;
    @Size(min = 20, max = 7000, message = "длин описания длжна быть min = 20, max = 7000")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @Min(0)
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120, message = "длин заглавия длжна быть min = 3, max = 120")
    private String title;
}
