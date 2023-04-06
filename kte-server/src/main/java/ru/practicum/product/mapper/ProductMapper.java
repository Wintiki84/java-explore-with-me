package ru.practicum.product.mapper;

import org.mapstruct.Mapper;
import ru.practicum.product.dto.ProductDto;
import ru.practicum.product.dto.ProductDtoRequest;
import ru.practicum.product.model.Product;
@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapToProduct(ProductDtoRequest productDtoRequest);
    ProductDto mapToProductDto(Product product);
}
