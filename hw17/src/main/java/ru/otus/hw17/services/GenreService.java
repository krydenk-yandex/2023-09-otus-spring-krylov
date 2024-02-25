package ru.otus.hw17.services;

import ru.otus.hw17.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);
}
