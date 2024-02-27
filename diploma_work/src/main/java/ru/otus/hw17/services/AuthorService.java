package ru.otus.hw17.services;

import ru.otus.hw17.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
