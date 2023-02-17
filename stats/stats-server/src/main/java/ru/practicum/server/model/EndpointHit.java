package ru.practicum.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "stats")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    @NotNull
    private String app;
    @Column(nullable = false)
    @NotNull
    private String uri;
    @Column(nullable = false)
    @NotNull
    private String ip;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp;
}
