package ru.practicum.server.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.user.dto.UserDto;

@Data
@Builder
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
