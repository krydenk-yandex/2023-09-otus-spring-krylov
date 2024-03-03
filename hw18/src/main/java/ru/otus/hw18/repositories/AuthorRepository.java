package ru.otus.hw18.repositories;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw18.models.Author;

@CircuitBreaker(name = "database")
public interface AuthorRepository extends JpaRepository<Author, Long> {}
