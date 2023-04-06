package ru.practicum.reviews.mapper;

import org.mapstruct.Mapper;
import ru.practicum.reviews.dto.ReviewsDto;
import ru.practicum.reviews.model.Reviews;

@Mapper(componentModel = "spring")
public interface ReviewsMapper {
    Reviews mapToReviews(ReviewsDto reviewsDto);
}
