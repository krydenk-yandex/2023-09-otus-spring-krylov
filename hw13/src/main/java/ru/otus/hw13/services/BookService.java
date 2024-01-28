package ru.otus.hw13.services;

import ru.otus.hw13.dto.BookDto;
import ru.otus.hw13.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    boolean existById(long id);

    Optional<Book> findById(long id);

    List<BookDto> findAll();

    Book insert(String title, long authorId, List<Long> genresIds);

    Book update(long id, String title, long authorId, List<Long> genresIds);

    void deleteById(long id);
}
