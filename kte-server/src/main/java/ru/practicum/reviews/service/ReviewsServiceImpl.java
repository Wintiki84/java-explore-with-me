package ru.practicum.reviews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.model.Client;
import ru.practicum.client.repository.ClientRepository;
import ru.practicum.product.model.Product;
import ru.practicum.product.repository.ProductRepository;
import ru.practicum.reviews.dto.ReviewsDto;
import ru.practicum.reviews.mapper.ReviewsMapper;
import ru.practicum.reviews.model.ReviewsPK;
import ru.practicum.reviews.repository.ReviewsRepository;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class ReviewsServiceImpl implements ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ReviewsMapper reviewsMapper;

    @Override
    @NotNull
    public void createReviews(@NotNull Long clientId, @NotNull Long productId, Integer star) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Клиент с id=" + clientId + " не найден"));
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Продукт id=" + productId + " не найден"));
        ReviewsPK reviewsPK = new ReviewsPK(client,product);
        if (star == null){
            reviewsRepository.deleteByPk(reviewsPK);
        } else {
            ReviewsDto reviewsDto = ReviewsDto
                    .builder()
                    .pk(reviewsPK)
                    .rating(star)
                    .build();
            reviewsRepository.save(reviewsMapper.mapToReviews(reviewsDto));
        }
    }
}
