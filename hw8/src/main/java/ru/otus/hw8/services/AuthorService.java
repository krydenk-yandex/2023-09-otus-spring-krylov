package ru.otus.hw8.services;

import ru.otus.hw8.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    Author save(Author author);

    void deleteById(String id);
}