package ru.otus.hw8.services;

import ru.otus.hw8.dto.BookDto;
import ru.otus.hw8.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(String id);

    List<BookDto> findAll();

    Book insert(String title, String authorId, List<String> genresIds);

    Book update(String id, String title, String authorId, List<String> genresIds);

    void deleteById(String id);
}
