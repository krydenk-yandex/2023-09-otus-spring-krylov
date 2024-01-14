package ru.otus.hw11.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw11.models.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, Long> { }
