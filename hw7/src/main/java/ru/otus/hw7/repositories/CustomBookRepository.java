package ru.otus.hw7.repositories;

import java.util.List;

import ru.otus.hw7.models.Book;

public interface CustomBookRepository {
    List<Book> findAllWithAuthorAndGenres();
}

