package ru.otus.hw18.repositories;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw18.models.Book;

import java.util.List;
import java.util.Optional;

@CircuitBreaker(name = "database")
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "book-with-author-and-genres")
    Optional<Book> findById(long id);

    @EntityGraph(value = "book-with-author")
    List<Book> findAll();
}
