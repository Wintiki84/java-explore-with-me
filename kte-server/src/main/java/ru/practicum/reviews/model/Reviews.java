package ru.practicum.reviews.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "shopping_list")
public class Reviews {
    @EmbeddedId
    private ReviewsPK pk;
    @Column(nullable = false)
    private Integer rating;
}
