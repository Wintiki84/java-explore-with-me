package ru.practicum.shopping.service;

import ru.practicum.shopping.dto.ShoppingDtoRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ShoppingService {
    @NotNull
    String RegistrationSale(@NotNull List<ShoppingDtoRequest> shoppingList, @NotNull Long clientId,
                            @NotNull Long totalPrice);
}
