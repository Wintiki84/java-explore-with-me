package ru.practicum.server.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull(message = "Не длжен быть null")
    private String title;
}
