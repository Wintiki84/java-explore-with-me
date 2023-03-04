package ru.practicum.server.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.location.Location;
import ru.practicum.server.user.dto.UserDto;


@Data
@Builder
public class EventDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Location location;
    private UserDto initiator;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
