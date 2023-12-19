package ru.otus.hw8.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw8.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    boolean existsBookByGenresId(String genreId);

    boolean existsBookByAuthorId(String id);

    Optional<Book> findById(String id);

    @Override
    List<Book> findAll();
}
