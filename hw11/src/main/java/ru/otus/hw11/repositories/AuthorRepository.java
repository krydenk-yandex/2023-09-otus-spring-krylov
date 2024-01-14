package ru.otus.hw11.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw11.models.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> { }
