package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validator.AdminDetails;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    @JsonProperty("app")
    @JsonView({AdminDetails.class})
    private String app;
    @JsonProperty("uri")
    @JsonView({AdminDetails.class})
    private String uri;
    @JsonProperty("hits")
    @JsonView({AdminDetails.class})
    private Long hits;
}
