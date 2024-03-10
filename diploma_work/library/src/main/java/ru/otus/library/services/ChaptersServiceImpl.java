package ru.otus.library.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.converters.ChapterConverter;
import ru.otus.library.dto.ChapterDto;
import ru.otus.library.dto.ChapterReducedDto;
import ru.otus.library.repositories.ChapterRepository;

@RequiredArgsConstructor
@Service
public class ChaptersServiceImpl implements ChaptersService {
    private final ChapterRepository chapterRepository;
    private final ChapterConverter chapterConverter;

    @Override
    @Transactional(readOnly = true)
    public Optional<ChapterDto> findByUuid(UUID uuid) {
        return chapterRepository.findByUuid(uuid).map(chapterConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChapterReducedDto> findAllByByBookId(Long bookId) {
        return chapterRepository.findAllByBookId(bookId)
                .stream().map(chapterConverter::toReducedDto).toList();
    }
}
