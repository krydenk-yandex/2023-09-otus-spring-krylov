package ru.otus.library.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.otus.library.dto.ChapterDto;
import ru.otus.library.dto.ChapterReducedDto;

public interface ChaptersService {
    Optional<ChapterDto> findByUuid(UUID uuid);

    List<ChapterReducedDto> findAllByByBookId(Long bookId);
}
