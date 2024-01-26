package ru.otus.hw11.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookWithAuthorIdAndGenresIds {
    private Long id;

    private String title;

    private Long authorId;

    private List<Long> genresIds;
}
