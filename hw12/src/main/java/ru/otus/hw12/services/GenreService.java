package ru.otus.hw12.services;

import ru.otus.hw12.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);
}
