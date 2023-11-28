package ru.otus.hw7.services;

import ru.otus.hw7.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);
}
