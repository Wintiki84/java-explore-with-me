package ru.practicum.webService.Impl;
/**
 * Имплиментция веб службы продуктов
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.cheque.dto.ChequeDtoResponce;
import ru.practicum.product.dto.ProductInfo;
import ru.practicum.product.model.Product;
import ru.practicum.cheque.service.ChequeService;
import ru.practicum.service.Interface.ProductService;
import ru.practicum.service.Interface.RatingService;
import ru.practicum.webService.Interface.ProductWebService;
import ru.practicum.model.util.*;

import javax.jws.WebService;
import java.util.List;


@Component
@WebService(serviceName = "ProductService", targetNamespace = "http://kte.test-web-service")
public class ProductWebServiceImpl implements ProductWebService {
    private ProductService productService;
    private ChequeService chequeService;
    private RatingService ratingService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setChequeService(ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    @Autowired
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public List<Product> getAllProduct() {
        return productService.allProduct();
    }

    @Override
    public ProductInfo getAdditionalProductInfo(long productId, long clientId) {
        return productService.additionalProductInfo(productId, clientId);
    }

    @Override
    public TotalPriceShopingListResponse totalPriceShopingLists(List<TotalPriceShopingListRequest> shopingList) {
        return productService.totalPriceResponse(shopingList);
    }

    @Override
    public ProductInfo feedBackProduct(long productId, long clientId, int amountStar) {
        ratingService.saveFeedbackProduct(productId, clientId, amountStar);

        return productService.additionalProductInfo(productId, clientId);
    }

    @Override
    public StatisticProduct productStatisctic(long productId) {
        return productService.statisticProduct(productId);
    }

    @Override
    public ChequeDtoResponce generateCheck(long clientId, double totalPrice, List<TotalPriceShopingListRequest> shopingList) {
        return chequeService.generateCheque(clientId, totalPrice, shopingList);
    }
}