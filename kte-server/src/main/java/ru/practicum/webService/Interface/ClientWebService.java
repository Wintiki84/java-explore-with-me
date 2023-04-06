package ru.practicum.webService.Interface;
/**
 * Soap веб служба клиентов
 */

import ru.practicum.client.model.Client;
import ru.practicum.client.dto.StatisticClient;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;


@WebService(name = "ClientService" ,targetNamespace = "http://kte.test-web-service")
public interface ClientWebService {
    /**
     * @return - список всех клиентов
     */
    @WebResult(name = "Client")
    @RequestWrapper(
            localName = "getAllClientRequest",
            className = "owl.home.KTE.test.webservice.AllClientRequest")
    @ResponseWrapper(
            localName = "getAllClientResponse",
            className = "owl.home.KTE.test.webservice.AllClientResponse")
    List<Client> getAllClient();

    /**
     * @param clientId - идентификатор клиента
     * @return - статистика клиента
     */
    @WebResult(name = "ClientStatistic")
    @RequestWrapper(
            localName = "getClientStatisticRequest",
            className = "owl.home.KTE.test.webservice.ClientStatisticRequest")
    @ResponseWrapper(
            localName = "getClientStatisticResponse",
            className = "owl.home.KTE.test.webservice.ClientStatisticResponse")
    StatisticClient getStatisticClient(@WebParam(name = "clientId") long clientId);

    /**
     * Обновление персональных скидок
     * @param clientId - идентификатор клиента
     * @param discount1 - скидка №1
     * @param discount2 - скидка №2
     * @return - клиент
     */
    @WebResult(name = "UpdateDiscount")
    @RequestWrapper(
            localName = "getUpdateDiscountRequest",
            className = "owl.home.KTE.test.webservice.UpdateDiscountRequest")
    @ResponseWrapper(
            localName = "getUpdateDiscountResponse",
            className = "owl.home.KTE.test.webservice.UpdateDiscountResponse")
    Client updateDiscount(
            @WebParam(name = "clientId") long clientId,
            @WebParam(name = "discount1") int discount1,
            @WebParam(name = "discount2") int discount2);
}