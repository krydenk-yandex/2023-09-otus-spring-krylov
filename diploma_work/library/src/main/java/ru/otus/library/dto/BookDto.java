package ru.otus.library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private long id;

    private String title;

    private String coverUrl;

    private AuthorDto author;

    private List<GenreDto> genres;
}
