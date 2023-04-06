package ru.practicum.shopping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.cheque.dto.ChequeDto;
import ru.practicum.cheque.mapper.ChequeMapper;
import ru.practicum.cheque.model.Cheque;
import ru.practicum.cheque.repository.ChequeRepository;
import ru.practicum.client.model.Client;
import ru.practicum.client.repository.ClientRepository;
import ru.practicum.product.model.Product;
import ru.practicum.product.repository.ProductRepository;
import ru.practicum.shopping.dto.ShoppingDto;
import ru.practicum.shopping.dto.ShoppingDtoRequest;
import ru.practicum.shopping.mapper.ShoppingMapper;
import ru.practicum.shopping.repository.ShoppingRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class ShoppingServiceImpl implements ShoppingService {
    private final ShoppingRepository shoppingRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ChequeRepository chequeRepository;
    private final ChequeMapper chequeMapper;
    private final ShoppingMapper shoppingMapper;

    @Override
    @NotNull
    public String RegistrationSale(@NotNull List<ShoppingDtoRequest> shoppingList, @NotNull Long clientId,
                            @NotNull Long totalPrice) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Клиент с id=" + clientId + " не найден"));
        Cheque chequeLast = chequeRepository.findTopByOrderByIdDesc();
        LocalDateTime currentDateTime = LocalDateTime.now();
        int numberCheque;
        if (currentDateTime.toLocalDate().equals(chequeLast.getDate().toLocalDate())){
            numberCheque = Integer.parseInt(chequeLast.getNumber()) + 1;
        } else {
            numberCheque = 100;
        }
        Cheque cheque = chequeRepository.save(chequeMapper.mapToCheque(ChequeDto
                .builder()
                .number(createCheckNumber(numberCheque))
                .totalPrice(totalPrice)
                .client(client)
                .date(currentDateTime)
                .build()));

        for (ShoppingDtoRequest shopping : shoppingList){
            Product product = productRepository.findById(shopping.getProduct()).orElseThrow(
                    () -> new IllegalArgumentException("Продукт id=" + shopping.getProduct() + " не найден"));
            int discount;
            if (shopping.getQuantity() < 5 || client.getPersonalDiscount2() == 0) {
                discount = product.getDiscount() + client.getPersonalDiscount1();
            } else {
                discount = product.getDiscount() + client.getPersonalDiscount2();
            }
            discount = Math.min(discount, 18);
            long price = (product.getPrice()*shopping.getQuantity() -
                    product.getPrice()*shopping.getQuantity()/100*discount);
            shoppingRepository.save(shoppingMapper.mapToShopping(ShoppingDto
                    .builder()
                            .cheque(cheque)
                            .product(product)
                            .client(client)
                            .quantity(shopping.getQuantity())
                            .price(product.getPrice() * shopping.getQuantity())
                            .totalDiscount(discount)
                            .totalPrice(price)
                    .build()));
        }
        return cheque.getNumber();
    }

    private static String createCheckNumber(int checkNumber) {
        String result = String.valueOf(checkNumber);
        if(result.length() < 4){
            result = String.format("%05d", checkNumber);
        }
        return result;
    }
}
