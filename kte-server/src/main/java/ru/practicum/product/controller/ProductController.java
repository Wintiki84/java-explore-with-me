package ru.practicum.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.util.StatisticProduct;
import ru.practicum.model.util.TotalPriceShopingListRequest;
import ru.practicum.model.util.TotalPriceShopingListResponse;
import ru.practicum.product.dto.ProductDto;
import ru.practicum.product.dto.ProductInfo;
import ru.practicum.product.model.Product;
import ru.practicum.product.service.ProductService;
import ru.practicum.reviews.service.ReviewsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/admin/product")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {
    private final ProductService productService;
    private final ReviewsService reviewsService;

    @GetMapping("/all")
    ResponseEntity<List<ProductDto>> getAllProduct(){
        return ResponseEntity.ok(productService.allProduct());
    }

    @GetMapping("/additional/{productId}/{clientId}")
    ResponseEntity<ProductInfo> getAdditionalProductInfo(
            @PathVariable("productId") long productId,
            @PathVariable("clientId") long clientId){
        return ResponseEntity.ok(productService.GettingProductInformation(clientId, productId));
    }

    /**
     * @param request - сервлетный запрос
     * @return - ответ итоговой стоимости товаров
     */
    @GetMapping("/total-price")
    ResponseEntity<TotalPriceShopingListResponse> totalPriceShopingLists(HttpServletRequest request){
        List<TotalPriceShopingListRequest> shopingList = totalPriseRequestList(request);

        return ResponseEntity.ok(productService.totalPriceResponse(shopingList));
    }

    /**
     * Оценка товара
     * @param productId - идентификатор продукта
     * @param clientId - идентификатор клиента
     * @param amountStar - количество звезд
     * @return - информация о продукте
     */
    @PutMapping("/feedback/{productId}/{clientId}/{amountStar}")
    ResponseEntity<ProductInfo> feedBackProduct(
            @PathVariable("productId") long productId,
            @PathVariable("clientId") long clientId,
            @PathVariable("amountStar") int amountStar
    ){

        ratingService.saveFeedbackProduct(productId, clientId, amountStar);

        ProductInfo productInfo = null;
        try {
            productInfo = productService.additionalProductInfo(productId, clientId);
        } catch (Exception e){
            if (e.getClass().equals(IllegalArgumentException.class) & e.getMessage().equals("Rating with this id not found!"))
                return ResponseEntity.ok(ratingService.createEmptyAdditonalProductInfo(productId, clientId));
        }

        return ResponseEntity.ok(productInfo);
    }

    /**
     * @param productId - идентификатор продукта
     * @return - статистика продукта
     */
    @GetMapping("/statistic/{productId}")
    ResponseEntity<StatisticProduct> productStatisctic(@PathVariable("productId") long productId){
        return ResponseEntity.ok(productService.statisticProduct(productId));
    }

    /**
     * @param clientId - идентификатор клиента
     * @param totalPrice - итоговая сумма
     * @param shopingList - список запроса итоговой стоимости товара
     * @return - чек
     */
    @PostMapping("/generate-check/{clientId}/{totalPrice}")
    ResponseEntity<ChequeDtoResponce> generateCheck(
            @PathVariable("clientId") long clientId,
            @PathVariable("totalPrice") double totalPrice,
            @RequestBody List<TotalPriceShopingListRequest> shopingList
    ){
        return ResponseEntity.ok(checkService.generateCheque(clientId, totalPrice, shopingList));
    }

    /**
     * Метод для отлова исключений которые прокидываются в серверном слое и отображения сообщения
     * @param exception - исключение которое проброшено
     * @return - ответ с кодом 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
