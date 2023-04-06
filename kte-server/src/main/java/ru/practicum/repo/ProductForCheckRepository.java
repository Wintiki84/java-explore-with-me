package ru.practicum.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.product.ProductForCheck;

import java.util.List;


@Repository
public interface ProductForCheckRepository extends JpaRepository<ProductForCheck, Long> {
    int countByProductId(long productId);
    List<ProductForCheck> findProductForCheckByproductId(long productId);
}
