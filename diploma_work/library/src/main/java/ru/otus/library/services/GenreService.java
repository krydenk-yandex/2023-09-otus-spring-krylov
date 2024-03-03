package ru.otus.library.services;

import ru.otus.library.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);
}
