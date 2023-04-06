package ru.practicum.shopping.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.cheque.model.Cheque;
import ru.practicum.client.model.Client;
import ru.practicum.product.model.Product;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "shopping_list")
public class Shopping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne
    @Column(name = "cheque_id")
    private Cheque cheque;
    @ManyToOne
    @Column(name = "product_id")
    private Product product;
    @ManyToOne
    @Column(name = "client_id", nullable = false)
    private Client client;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Long price;
    @Column(name = "total_dxiscount", nullable = false)
    private Integer totalDiscount;
    @Column(name = "total_price", nullable = false)
    private Long totalPrice;
}
