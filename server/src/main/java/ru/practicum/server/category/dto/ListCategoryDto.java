package ru.practicum.server.category.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Details;
import ru.practicum.validator.Private;

import java.util.List;

@Builder
@Getter
public class ListCategoryDto {
    @JsonView({AdminDetails.class, Private.class, Details.class})
    @JsonValue
    private List<CategoryDto> catList;
}
