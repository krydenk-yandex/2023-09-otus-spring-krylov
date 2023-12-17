package ru.otus.hw8.services;

import ru.otus.hw8.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<String> ids);

    Genre save(Genre genre);

    void deleteById(String id);
}
