package ru.practicum.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.client.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
