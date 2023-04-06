package ru.practicum.webService.Impl;
/**
 * Имплиментация веб службы клиентов
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.client.model.Client;
import ru.practicum.client.dto.StatisticClient;
import ru.practicum.client.service.ClientService;
import ru.practicum.webService.Interface.ClientWebService;

import javax.jws.WebService;
import java.util.List;


@Component
@WebService(serviceName = "ClientService", targetNamespace = "http://kte.test-web-service")
public class ClientWebServiceImpl implements ClientWebService {
    ClientService service;

    @Autowired
    public void setService(ClientService service) {
        this.service = service;
    }

    @Override
    public List<Client> getAllClient() {
        return service.allClient();
    }

    @Override
    public StatisticClient getStatisticClient(long clientId) {
        return service.statisticClient(clientId);
    }

    @Override
    public Client updateDiscount(long clientId, int discount1, int discount2) {
        return service.updateDiscounts(clientId, discount1, discount2);
    }
}