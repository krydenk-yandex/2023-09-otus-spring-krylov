package ru.otus.hw17.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw17.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
