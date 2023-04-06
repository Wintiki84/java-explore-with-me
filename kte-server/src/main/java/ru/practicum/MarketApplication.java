package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.practicum.product.service.ProductService;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

	@Autowired
	ProductService productService;

	@Async
	@Scheduled(cron = "0 0 * * * *")
	public void clearAndSetDiscount(){
		productService.clearAndSetDiscountColumn();
	}
}