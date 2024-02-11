package ru.otus.hw14.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw14.models.GenreMongo;

public interface GenreRepository extends MongoRepository<GenreMongo, String> {}
