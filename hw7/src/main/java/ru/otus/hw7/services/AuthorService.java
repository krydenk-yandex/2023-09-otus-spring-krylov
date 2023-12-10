package ru.otus.hw7.services;

import ru.otus.hw7.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
