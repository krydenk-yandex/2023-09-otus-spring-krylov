package ru.otus.hw12.services;

import ru.otus.hw12.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
