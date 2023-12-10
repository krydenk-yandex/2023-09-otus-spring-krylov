package ru.otus.hw6.services;

import ru.otus.hw6.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
