package ru.practicum.shopping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingDtoRequest {
    private Long product;
    private Integer quantity;
}
