package ru.practicum.webService;


import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.webService.Impl.ClientWebServiceImpl;
import ru.practicum.webService.Impl.ProductWebServiceImpl;

import javax.xml.ws.Endpoint;


@Configuration
public class WebServiceConfig {
    @Bean
    public Endpoint clientEndpoint(Bus bus, ClientWebServiceImpl clientWebService){
        EndpointImpl endpoint = new EndpointImpl(bus, clientWebService);
        endpoint.publish("/ClientService");

        return endpoint;
    }

    @Bean
    public Endpoint productEndpoint(Bus bus, ProductWebServiceImpl productWebService){
        EndpointImpl endpoint = new EndpointImpl(bus, productWebService);
        endpoint.publish("/ProductService");

        return endpoint;
    }
}