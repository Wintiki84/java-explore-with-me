package ru.practicum.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shopping.model.Shopping;

@Repository
public interface ShoppingRepository extends JpaRepository<Shopping, Long> {
    Long countByClientIdAndProductId(Long clientId, Long ProductId);
    @Query("SELECT SUM(Shopping.totalPrice) FROM Shopping s WHERE s.client.id = ?1 AND s.product.id = ?2")
    Long sumTotalOriginalPrise(Long clientId, Long ProductId);
    @Query("SELECT SUM(Shopping.totalDiscount) FROM Shopping s WHERE s.client.id = ?1 AND s.product.id = ?2")
    Long sumTotalDiscount(Long clientId, Long ProductId);

}
