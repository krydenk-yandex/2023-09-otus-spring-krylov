package ru.otus.library.services;

import ru.otus.library.dto.BookDto;
import ru.otus.library.models.Book;

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