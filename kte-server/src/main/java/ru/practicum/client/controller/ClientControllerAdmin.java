package ru.practicum.client.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.dto.ClientDto;
import ru.practicum.client.model.Client;
import ru.practicum.client.service.ClientService;
import ru.practicum.client.dto.StatisticClient;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Create;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/rest/client")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientControllerAdmin {
    private final ClientService clientService;

    @JsonView({AdminDetails.class})
    @PostMapping
    public ResponseEntity<ClientDto> createUser(@Validated(Create.class) @RequestBody ClientDto clientDto) {
        log.info("создание пользователя");
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createUser(clientDto));
    }

    @JsonView(AdminDetails.class)
    @DeleteMapping("{clientId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long clientId) {
        log.info("удалить пользователя с id {}", clientId);
        clientService.deleteUser(clientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @JsonView(AdminDetails.class)
    @GetMapping("/all")
    ResponseEntity<List<Client>> getAllClient(){
        log.info("Получить список всех клиентов");
        return ResponseEntity.ok(clientService.allClient());
    }

    @JsonView(AdminDetails.class)
    @PatchMapping("{clientId}/update/{discount1}/{discount2}")
    ResponseEntity<ClientDto> updateDiscount(
            @PathVariable @Positive Long clientId,
            @PathVariable @Min(0) Integer discount1,
            @PathVariable @Min(0) Integer discount2){
        log.info("Обновить скидки: clientId: {}, discount1: {},discount2: {}", clientId, discount1, discount2);
        return ResponseEntity.ok(clientService.updateDiscounts(clientId, discount1, discount2));
    }

    @JsonView(AdminDetails.class)
    @GetMapping("{clientId}/statistic/{productId}/")
    ResponseEntity<StatisticClient> clientStatistic(@PathVariable @Positive Long clientId,
                                                            @PathVariable @Positive Long productId){
        log.info("Получить статистику пользователя: clientId: {}, productId: {}", clientId, productId);
        return ResponseEntity.ok(clientService.statisticClient(clientId, productId));
    }
}
