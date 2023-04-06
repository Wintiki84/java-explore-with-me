package ru.practicum.product.service;

import ru.practicum.product.dto.ProductDto;
import ru.practicum.product.dto.ProductDtoRequest;
import ru.practicum.product.dto.ProductInfo;
import ru.practicum.shopping.dto.ShoppingDtoRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ProductService {
    @NotNull
    ProductDto findProductById(@NotNull Long productId);
    void deleteProductById(@NotNull Long productId);
    @NotNull
    ProductDto saveProduct(@NotNull ProductDtoRequest productDtoRequest);
    @NotNull
    List<ProductDto> allProduct();
    @NotNull
    ProductInfo GettingProductInformation(@NotNull Long clientId, @NotNull Long productId);
    @NotNull
    Long getFinalPrice(@NotNull List<ShoppingDtoRequest> shoppingList, @NotNull Long clientId);
}
