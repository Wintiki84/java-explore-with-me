package ru.practicum.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.dto.ClientDto;
import ru.practicum.client.mapper.ClientMapper;
import ru.practicum.client.model.Client;
import ru.practicum.client.repository.ClientRepository;
import ru.practicum.client.dto.StatisticClient;
import ru.practicum.handler.exception.NotFoundException;
import ru.practicum.shopping.repository.ShoppingRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ShoppingRepository shoppingRepository;
    private final ClientMapper mapper;

    @NotNull
    @Override
    @Transactional
    public ClientDto createUser(@NotNull ClientDto clientDto) {
        return mapper.mapToUserDto(clientRepository.save(mapper.mapToUser(clientDto)));
    }

    @Override
    @Transactional
    public void deleteUser(@NotNull Long clientId) {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        } else {
            throw new NotFoundException("Пользователь с id=" + clientId + " не найден");
        }
    }

    @NotNull
    @Override
    public List<Client> allClient(){return clientRepository.findAll();}

    @NotNull
    @Override
    @Transactional
    public ClientDto updateDiscounts(@NotNull Long clientId, @NotNull Integer discount1, @NotNull Integer discount2) {
        clientRepository.updateDiscount(clientId, discount1, discount2);
        if (clientRepository.existsById(clientId)) {
            clientRepository.updateDiscount(clientId, discount1, discount2);;
        } else {
            throw new NotFoundException("Пользователь с id=" + clientId + " не найден");
        }
        return mapper.mapToUserDto(findByClientId(clientId));
    }

    @NotNull
    @Override
    public StatisticClient statisticClient(@NotNull Long clientId, @NotNull Long productId) {
        return StatisticClient
                .builder()
                .numberOfReceipts(shoppingRepository.countByClientIdAndProductId(clientId, productId))
                .totalChequeSum(shoppingRepository.sumTotalOriginalPrise(clientId, productId))
                .totalChequeDiscountSum(shoppingRepository.sumTotalDiscount(clientId, productId))
                .build();
    }

    private Client findByClientId(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + clientId + " не найден"));
    }
}
