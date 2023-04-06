package ru.practicum.product.dto;

import lombok.*;

import java.util.Map;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class ProductInfo {
    private String name;
    private String description;
    private Double middleStar;
    private Integer userRating;
    private Map<Integer, Long> starsDistribution;
}