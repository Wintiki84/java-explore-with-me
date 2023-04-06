package ru.practicum.reviews.service;

import javax.validation.constraints.NotNull;

public interface ReviewsService {
    @NotNull
    void createReviews(@NotNull Long clientId, @NotNull Long productId, Integer star);
}
