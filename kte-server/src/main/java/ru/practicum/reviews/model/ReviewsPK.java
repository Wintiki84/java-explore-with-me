package ru.practicum.reviews.model;

import lombok.*;
import ru.practicum.cheque.model.Cheque;
import ru.practicum.client.model.Client;
import ru.practicum.product.model.Product;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ReviewsPK implements Serializable {
    @ManyToOne
    @Column(name = "client_id")
    private Client client;
    @ManyToOne
    @Column(name = "product_id")
    private Product product;
}
