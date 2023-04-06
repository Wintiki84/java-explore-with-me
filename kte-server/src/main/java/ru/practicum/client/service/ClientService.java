package ru.practicum.client.service;

import ru.practicum.client.dto.ClientDto;
import ru.practicum.client.dto.StatisticClient;
import ru.practicum.client.model.Client;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ClientService {
    @NotNull
    ClientDto createUser(@NotNull ClientDto clientDto);

    void deleteUser(@NotNull Long clientId);

    @NotNull
    List<Client> allClient();

    @NotNull
    ClientDto updateDiscounts(@NotNull Long clientId, @NotNull Integer discount1, @NotNull Integer discount2);

    @NotNull
    StatisticClient statisticClient(@NotNull Long clientId, @NotNull Long productId);
}
