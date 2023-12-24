package ru.otus.hw9.services;

import ru.otus.hw9.dto.BookDto;
import ru.otus.hw9.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(long id);

    List<BookDto> findAll();

    Book insert(String title, long authorId, List<Long> genresIds);

    Book update(long id, String title, long authorId, List<Long> genresIds);

    void deleteById(long id);
}
