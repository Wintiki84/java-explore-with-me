package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.validator.Details;
import ru.practicum.validator.Private;

@Data
@Builder
public class EventShortDto {
    @JsonView({Details.class, Private.class})
    private Long id;
    @JsonView({Details.class, Private.class})
    private String annotation;
    @JsonView({Details.class, Private.class})
    private CategoryDto category;
    @JsonView({Details.class, Private.class})
    private Integer confirmedRequests;
    @JsonView({Details.class, Private.class})
    private String eventDate;
    @JsonView({Details.class, Private.class})
    private UserDto initiator;
    @JsonView({Details.class, Private.class})
    private Boolean paid;
    @JsonView({Details.class, Private.class})
    private String title;
    @JsonView({Details.class, Private.class})
    private Long views;
}
