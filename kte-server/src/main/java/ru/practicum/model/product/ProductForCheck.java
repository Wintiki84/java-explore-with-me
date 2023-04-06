package ru.practicum.model.product;
/**
 * Специальный класс который служит в качестве объединения чека и продукта,
 * по этой причине содержит ограничение уникальности столбцов product id - client id.
 * Так же одержит поля количества продукта в этом чеке и суммы всех скидок
 */

import lombok.*;
import ru.practicum.product.model.Product;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(
        name = "product_for_check",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "check_id"}))
@Getter @Setter @Builder @EqualsAndHashCode(exclude = "id")  @AllArgsConstructor @NoArgsConstructor
public class ProductForCheck implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "amount_product")
    private int amountProduct;
    @Column(name = "sum_discount")
    private int sumDiscount;
}
