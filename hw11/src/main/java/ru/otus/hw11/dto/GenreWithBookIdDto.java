package ru.otus.hw11.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenreWithBookIdDto {
    private long id;

    private String name;

    private long bookId;
}
