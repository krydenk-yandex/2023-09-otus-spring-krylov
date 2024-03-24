package ru.otus.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
