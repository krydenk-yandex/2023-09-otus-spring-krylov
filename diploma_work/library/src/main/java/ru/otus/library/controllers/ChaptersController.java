package ru.otus.library.controllers;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.dto.ChapterDto;
import ru.otus.library.dto.ChapterReducedDto;
import ru.otus.library.exceptions.EntityNotFoundException;
import ru.otus.library.services.ChaptersService;

@RestController
@RequiredArgsConstructor
public class ChaptersController {
    private final ChaptersService chaptersService;

    @GetMapping("/api/chapters/by-book/{bookId}")
    public List<ChapterReducedDto> chaptersList(@PathVariable Long bookId) {
        return chaptersService.findAllByByBookId(bookId);
    }

    @GetMapping("/api/chapters/{uuid}")
    public ChapterDto getChapter(@PathVariable UUID uuid) {
        return chaptersService.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("The chapter was not found"));
    }
}
