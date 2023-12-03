package ru.otus.hw7.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw7.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "book-with-author-and-genres")
    Optional<Book> findById(long id);

    @EntityGraph(value = "book-with-author")
    List<Book> findAll();
}
