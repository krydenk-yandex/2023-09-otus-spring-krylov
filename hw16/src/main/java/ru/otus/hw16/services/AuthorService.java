package ru.otus.hw16.services;

import ru.otus.hw16.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
