package ru.otus.hw9.services;

import ru.otus.hw9.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
