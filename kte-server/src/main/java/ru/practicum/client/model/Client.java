package ru.practicum.client.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "client")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "personal_discount1")
    private Integer personalDiscount1;
    @Column(name = "personal_discount2")
    private Integer personalDiscount2;
}
