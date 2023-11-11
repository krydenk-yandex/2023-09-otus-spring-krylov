package ru.otus.hw5.repositories;

import ru.otus.hw5.models.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);
}
