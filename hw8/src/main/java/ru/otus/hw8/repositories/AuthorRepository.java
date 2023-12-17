package ru.otus.hw8.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw8.models.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {}
