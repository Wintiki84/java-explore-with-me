package ru.practicum.shopping.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.cheque.model.Cheque;
import ru.practicum.client.model.Client;
import ru.practicum.product.model.Product;

@Data
@Builder
public class ShoppingDto {
    private Cheque cheque;
    private Product product;
    private Client client;
    private Integer quantity;
    private Long price;
    private Integer totalDiscount;
    private Long totalPrice;
}
