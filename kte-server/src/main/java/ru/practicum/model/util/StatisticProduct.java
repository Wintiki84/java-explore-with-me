package ru.practicum.model.util;
/**
 * Статистика продукта, включает в себя:
 * продукт, количество чеко в которых есть этот продукт,
 * итоговая стоимость по стоимости без скидок, сумму скидок
 */

import lombok.*;
import ru.practicum.product.model.Product;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class StatisticProduct {
    private Product product;
    private int amountCheck;
    private double totalOriginalPrise;
    private int discountSum;
}
