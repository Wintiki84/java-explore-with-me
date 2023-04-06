package ru.practicum.product.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Private;

@Data
@Builder
public class ProductDto {
    @JsonView({AdminDetails.class, Private.class})
    private Long id;
    @JsonView({AdminDetails.class, Private.class})
    private String name;
    @JsonView({AdminDetails.class, Private.class})
    private Long price;
}
