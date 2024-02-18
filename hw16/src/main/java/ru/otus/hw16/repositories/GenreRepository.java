package ru.otus.hw16.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw16.models.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByIdIn(List<Long> ids);
}
