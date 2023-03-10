package ru.practicum.server.compilation.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Details;

import java.util.List;

@Data
@Builder
public class CompilationDtoResponse {
    @JsonView({Details.class, AdminDetails.class})
    private Long id;
    @JsonView({Details.class, AdminDetails.class})
    private List<EventShortDto> events;
    @JsonView({Details.class, AdminDetails.class})
    private Boolean pinned;
    @JsonView({Details.class, AdminDetails.class})
    private String title;

}
