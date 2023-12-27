package ru.otus.hw10.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw10.models.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByIdIn(List<Long> ids);
}
