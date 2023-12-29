package ru.otus.hw10.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw10.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
