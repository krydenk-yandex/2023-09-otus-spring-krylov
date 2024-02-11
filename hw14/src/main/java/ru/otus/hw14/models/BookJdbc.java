package ru.otus.hw14.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookJdbc {
    private long id;

    private String title;

    private long authorId;

    private long genreId;
}
