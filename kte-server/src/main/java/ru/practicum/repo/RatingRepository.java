package ru.practicum.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.product.Rating;

import java.util.List;
import java.util.Optional;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProductId(long productId);
    Optional<Rating> findByProductIdAndClientId(long productId, long clientId);

    /**
     * Удаляет рйтинг по id
     * @param id - id рейтинга
     */
    @Modifying
    @Query(value = "delete from rating_table where id = :id", nativeQuery = true)
    void deleteRatingById(@Param("id") long id);
}
