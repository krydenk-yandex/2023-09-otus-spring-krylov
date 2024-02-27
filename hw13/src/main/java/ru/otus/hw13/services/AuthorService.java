package ru.otus.hw13.services;

import ru.otus.hw13.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
