package ru.otus.hw10.services;

import ru.otus.hw10.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
