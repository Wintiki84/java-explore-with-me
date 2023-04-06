package ru.practicum.client.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;

import ru.practicum.client.model.Client;
import ru.practicum.validator.AdminDetails;

import java.util.List;

@Builder
@Getter
public class ClientList {
    @JsonView({AdminDetails.class})
    @JsonValue
    private List<Client> clients;
}
