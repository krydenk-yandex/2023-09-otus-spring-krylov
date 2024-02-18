package ru.otus.hw16.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw16.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
