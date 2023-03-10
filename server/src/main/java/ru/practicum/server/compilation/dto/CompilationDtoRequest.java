package ru.practicum.server.compilation.dto;

import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Data;
import ru.practicum.validator.Create;
import ru.practicum.validator.Update;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CompilationDtoRequest {
    @NotNull(groups = {Create.class, Update.class}, message = "Не длжен быть null")
    private List<Long> events;
    @BooleanFlag
    private Boolean pinned;
    @NotNull(groups = {Create.class}, message = "Не длжен быть null")
    private String title;
}
