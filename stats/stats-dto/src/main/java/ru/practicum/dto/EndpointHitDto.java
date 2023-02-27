package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Create;
import ru.practicum.validator.DateValidator;
import ru.practicum.validator.Details;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class EndpointHitDto {
    static final String IPV4_REGEX =
            "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
    private Long id;
    @JsonView({Details.class, AdminDetails.class})
    @NotEmpty(groups = {Create.class}, message = "неправильный APP")
    @NotNull(groups = {Create.class}, message = "APP не может быть NULL")
    private String app;
    @JsonView({Details.class, AdminDetails.class})
    @URL(groups = {Create.class}, protocol = "http", message = "неправильный URI")
    @NotNull(groups = {Create.class}, message = "URI не может быть NULL")
    private String uri;
    @JsonView({Details.class, AdminDetails.class})
    @Pattern(groups = {Create.class}, regexp = IPV4_REGEX, message = "неправильный IP")
    @NotNull(groups = {Create.class}, message = "IP не может быть NULL")
    private String ip;
    @NotNull(groups = {Create.class}, message = "timestamp не может быть NULL")
    @DateValidator
    private String timestamp;
}
