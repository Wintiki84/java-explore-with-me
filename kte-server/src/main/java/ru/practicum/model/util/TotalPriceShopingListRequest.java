package ru.practicum.model.util;
/**
 * Класс запроса итоговой стоимости, является объединением идентификатора продукта
 * и количества продукта, используется для расчета итоговой стоимости чека.
 */

import lombok.*;


@Getter @Setter @Builder @AllArgsConstructor@NoArgsConstructor
public class TotalPriceShopingListRequest {
    private long productId;
    private int amount;
}
