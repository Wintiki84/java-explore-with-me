package ru.practicum.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.practicum.product.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    Long updateDistinctById(Long id);
}
