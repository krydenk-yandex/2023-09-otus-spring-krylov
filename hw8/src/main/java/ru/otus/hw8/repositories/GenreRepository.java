package ru.otus.hw8.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw8.models.Genre;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findByIdIn(List<String> ids);
}
