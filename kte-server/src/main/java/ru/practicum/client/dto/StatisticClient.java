package ru.practicum.client.dto;

import lombok.*;
import ru.practicum.client.model.Client;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class StatisticClient {
    private Long numberOfReceipts;
    private Long totalChequeSum;
    private Long totalChequeDiscountSum;
}
