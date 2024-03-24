package ru.otus.library.services;

import ru.otus.library.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
