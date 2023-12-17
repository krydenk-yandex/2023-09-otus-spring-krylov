package ru.otus.hw8.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw8.models.Book;
import ru.otus.hw8.models.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    boolean existsBookByGenresContains(List<Genre> genres);

    boolean existsBookByAuthorId(String id);

    Optional<Book> findById(String id);

    @Override
    List<Book> findAll();
}
