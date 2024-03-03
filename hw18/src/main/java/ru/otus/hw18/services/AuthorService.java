package ru.otus.hw18.services;

import ru.otus.hw18.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
