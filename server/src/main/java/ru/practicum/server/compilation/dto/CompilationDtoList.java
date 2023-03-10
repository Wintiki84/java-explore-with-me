package ru.practicum.server.compilation.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Details;

import java.util.List;

@Builder
@Getter
public class CompilationDtoList {
    @JsonValue
    @JsonView({Details.class, AdminDetails.class})
    private List<CompilationDtoResponse> compilations;
}
