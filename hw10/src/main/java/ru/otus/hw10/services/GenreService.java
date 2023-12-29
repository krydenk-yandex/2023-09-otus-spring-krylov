package ru.otus.hw10.services;

import ru.otus.hw10.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);
}
