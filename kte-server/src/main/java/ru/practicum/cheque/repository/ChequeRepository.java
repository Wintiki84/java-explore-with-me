package ru.practicum.cheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.cheque.model.Cheque;

@Repository
public interface ChequeRepository extends JpaRepository<Cheque, Long> {
    Cheque findTopByOrderByIdDesc();
}
