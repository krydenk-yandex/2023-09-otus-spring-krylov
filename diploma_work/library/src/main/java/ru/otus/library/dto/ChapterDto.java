package ru.otus.library.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChapterDto {
    private UUID uuid;

    private String title;

    private String text;

    private BookReducedDto book;

    private ChapterReducedDto nextChapter;

    private ChapterReducedDto prevChapter;
}
