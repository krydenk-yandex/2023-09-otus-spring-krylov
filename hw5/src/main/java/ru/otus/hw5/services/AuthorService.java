package ru.otus.hw5.services;

import ru.otus.hw5.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
