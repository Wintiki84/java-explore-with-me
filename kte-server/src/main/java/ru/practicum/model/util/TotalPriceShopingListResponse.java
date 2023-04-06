package ru.practicum.model.util;
/**
 * Класс ответа итоговой стоимости, является объединением итоговой стоимости всего чека и скидки на него.
 */

import lombok.*;
import ru.practicum.service.util.Carrensy;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class TotalPriceShopingListResponse {
    private double totalPrice;
    private Carrensy carrensy;
}
