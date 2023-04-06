package ru.practicum.cheque.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.client.model.Client;

import java.time.LocalDateTime;

@Data
@Builder
public class ChequeDto {
    private String number;
    private Long totalPrice;
    private Client client;
    private LocalDateTime date;
}
