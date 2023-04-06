package ru.practicum.product.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Column(nullable = false)
    private Long price;
    @Column
    private Integer discount;
}