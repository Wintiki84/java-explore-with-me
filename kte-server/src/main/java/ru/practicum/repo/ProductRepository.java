package ru.practicum.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.product.model.Product;


@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>,
        QuerydslPredicateExecutor<Product> {
    @Modifying
    @Query(value = "update  set product_discount = 0", nativeQuery = true)
    void clearDiscountColumn();
}
