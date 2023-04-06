package ru.practicum.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.client.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Modifying
    @Query("UPDATE Client c SET c.personalDiscount1= ?2, c.personalDiscount2 = ?3 WHERE ?1")
    void updateDiscount(Long id, Integer discount1, Integer discount2);
}
