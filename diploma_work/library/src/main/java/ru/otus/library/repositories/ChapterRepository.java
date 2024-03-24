package ru.otus.library.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.library.models.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, UUID> {

    @EntityGraph(attributePaths = {"book", "book.author", "book.genres", "nextChapter", "prevChapter"})
    Optional<Chapter> findByUuid(UUID uuid);

    List<Chapter> findAllByBookId(Long bookId);
}
