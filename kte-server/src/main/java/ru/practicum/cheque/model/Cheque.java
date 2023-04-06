package ru.practicum.cheque.model;

import lombok.*;
import ru.practicum.client.model.Client;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "client")
public class Cheque implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(name = "total_price", nullable = false)
    private Long totalPrice;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();
}
