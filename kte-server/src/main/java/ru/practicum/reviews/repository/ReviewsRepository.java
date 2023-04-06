package ru.practicum.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.product.model.Product;
import ru.practicum.reviews.model.Reviews;
import ru.practicum.reviews.model.ReviewsPK;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    Reviews findByPk(ReviewsPK reviewsPK);
    List<Reviews> findAllByPk_Product(Product product);
    @Query("SELECT rating, COUNT(*) as count FROM Reviews WHERE Product = ?1 GROUP BY rating")
    Map<Integer, Long> findStar (Product product);
    @Query("SELECT ROUND(AVG(rating), 1) AS avg_rating FROM Reviews WHERE Product = ?1")
    Double findMidlStar (Product product);
    void deleteByPk(ReviewsPK reviewsPK);
}

