package ru.otus.library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookWithChaptersDto {
    private long id;

    private String title;

    private String coverUrl;

    private AuthorDto author;

    private List<GenreDto> genres;

    private List<ChapterDto> chapters;
}