package ru.practicum.client.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.practicum.client.dto.ClientDto;
import ru.practicum.client.model.Client;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client mapToUser(ClientDto clientDto);

    ClientDto mapToUserDto(Client client);

    List<Client> mapToUserList(Page<Client> page);
}
