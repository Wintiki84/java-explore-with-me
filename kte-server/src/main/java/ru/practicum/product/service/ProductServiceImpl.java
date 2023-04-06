package ru.practicum.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.model.Client;
import ru.practicum.client.repository.ClientRepository;
import ru.practicum.handler.exception.NotFoundException;
import ru.practicum.product.dto.ProductDto;
import ru.practicum.product.dto.ProductDtoRequest;
import ru.practicum.product.dto.ProductInfo;
import ru.practicum.product.mapper.ProductMapper;
import ru.practicum.product.model.Product;
import ru.practicum.product.repository.ProductRepository;
import ru.practicum.reviews.model.Reviews;
import ru.practicum.reviews.model.ReviewsPK;
import ru.practicum.reviews.repository.ReviewsRepository;
import ru.practicum.shopping.dto.ShoppingDtoRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ReviewsRepository reviewsRepository;
    private final ProductMapper mapper;

    @Override
    @NotNull
    public ProductDto findProductById(@NotNull Long productId) {
        return mapper.mapToProductDto(productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Продукт id=" + productId + " не найден")));
    }

    @Override
    @Transactional
    public void deleteProductById(@NotNull Long productId){
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new NotFoundException("Продукт id=" + productId + " не найден");
        }
    }

    @Override
    @Transactional
    public ProductDto saveProduct(@NotNull ProductDtoRequest productDtoRequest) {
        return mapper.mapToProductDto(productRepository.save(mapper.mapToProduct(productDtoRequest)));
    }
    @Override
    @NotNull
    public List<ProductDto> allProduct() {
        return productRepository.findAll().stream().map(mapper::mapToProductDto).collect(Collectors.toList());
    }

    @Override
    @NotNull
    public ProductInfo GettingProductInformation(@NotNull Long clientId, @NotNull Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Продукт id=" + productId + " не найден"));

        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Клиент с id=" + clientId + " не найден"));

        ReviewsPK reviewsPK = new ReviewsPK(client, product);

        Reviews reviews = reviewsRepository.findByPk(reviewsPK);

        Map<Integer, Long> distributionStar = reviewsRepository.findStar(product);

        Double middleStar = reviewsRepository.findMidlStar(product);

        return ProductInfo
                .builder()
                .name(product.getName())
                .description(product.getDescription())
                .middleStar(middleStar)
                .userRating(reviews.getRating())
                .starsDistribution(distributionStar)
                .build();
    }

    @Override
    @NotNull
    public Long getFinalPrice(@NotNull List<ShoppingDtoRequest> shoppingList, @NotNull Long clientId){
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Клиент с id=" + clientId + " не найден"));
        long totalPrice = 0L;
        for (ShoppingDtoRequest shopping : shoppingList){
            Product product = productRepository.findById(shopping.getProduct()).orElseThrow(
                    () -> new IllegalArgumentException("Продукт id=" + shopping.getProduct() + " не найден"));
            int discount;
            if (shopping.getQuantity() < 5 || client.getPersonalDiscount2() == 0) {
                discount = product.getDiscount() + client.getPersonalDiscount1();
            } else {
                discount = product.getDiscount() + client.getPersonalDiscount2();
            }
            discount = Math.min(discount, 18);
            totalPrice = totalPrice + (product.getPrice()*shopping.getQuantity() -
                    product.getPrice()*shopping.getQuantity()/100*discount);
        }
        return totalPrice;
    }
}
