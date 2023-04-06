package ru.practicum.reviews.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.reviews.model.ReviewsPK;

@Data
@Builder
public class ReviewsDto {
    private ReviewsPK pk;
    private Integer rating;
}
